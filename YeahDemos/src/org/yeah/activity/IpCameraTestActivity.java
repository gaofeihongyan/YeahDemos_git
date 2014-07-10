
package org.yeah.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import org.yeah.R;

import spider.szc.CameraRec;
import spider.szc.ColorArray;
import spider.szc.ProgressAsynTask;
import spider.szc.ThreadPoolUtil;
import spider.szc.ToolUtil;
import spider.szc.VideoQuality;
import spider.szc.XmlRequstTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressLint("NewApi")
public class IpCameraTestActivity extends Activity {

    protected static final String TAG = "harry";

    private SurfaceView mSurfaceView;



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.one_split);

        mSurfaceView = (SurfaceView) findViewById(R.id.surView_one);

        initCameraList();

    }

    private int userClickCount = 1;
    private long oldTime;

    @Override
    public void onBackPressed() {

        if (oldTime == 0) {
            oldTime = System.currentTimeMillis();
        }
        long timeCount = System.currentTimeMillis() - oldTime;
        // Log.d(Util.TAG, "Start Activity --> timeCount: " + timeCount);
        if (timeCount > 100) {
            if (timeCount < 1000) {
                // finish();
                super.onBackPressed();
                return;
            } else {
                oldTime = 0;
                readyToShowVideo();
                Toast.makeText(this, "readyToShowVideo", Toast.LENGTH_SHORT).show();
            }
        }

        // if (userClickCount == 2) {
        // // finish();
        // super.onBackPressed();
        // } else {
        // userClickCount++;
        // readyToShowVideo();
        // Toast.makeText(this, "readyToShowVideo", Toast.LENGTH_SHORT).show();
        // }

    }

    /***************************************************** 华丽的分割线 ***********************************************************/

    private TaskQueue taskQueue = new TaskQueue();
    private XmlRequstTool httpRequstTool;
    public static ArrayList<CameraRec> cameraList;
    public static ArrayList<CameraRec> mainlist_camera_action;

    /**
     * initMonitor
     */
    private void initCameraList() {
        httpRequstTool = new XmlRequstTool();
        cameraList = new ArrayList<CameraRec>();
        mainlist_camera_action = new ArrayList<CameraRec>();
        VideoQuality.initVideoQuality();
        ToolUtil.setVideoStream(ToolUtil.VIDEO_PC_STREAM);
        taskQueue.start();
        initData();
    }

    private class TaskQueue extends Thread {

        private LinkedBlockingQueue<Runnable> queue;
        private boolean isLoop = true;

        public TaskQueue() {
            queue = new LinkedBlockingQueue<Runnable>();
        }

        public void addTask(Runnable task) {
            queue.offer(task);
        }

        public void stopLoop() {
            this.isLoop = false;
            queue.offer(new Runnable() {
                @Override
                public void run() {
                    return;
                }
            });
        }

        public void deleteAllTask() {
            queue.clear();
        }

        @Override
        public void run() {
            while (isLoop) {
                try {
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initData() {
        new ProgressAsynTask<Void, Void, ArrayList<CameraRec>>(this, R.string.getdevice) {
            @Override
            protected ArrayList<CameraRec> doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cameraList = (ArrayList<CameraRec>) httpRequstTool.getAllDevice();
                Log.d("harry", "*********************cameraList.size():" + cameraList.size());
                return cameraList;
            }

            @Override
            protected void onPostExecute(ArrayList<CameraRec> result) {
                if (result.size() != 0) {
                    connectAllDevice();
                }
                super.onPostExecute(result);
            }
        }.execute();

    }

    private void connectAllDevice() {
        taskQueue.deleteAllTask();
        for (int i = 0; i < cameraList.size(); i++) {
            CameraRec camera = cameraList.get(i);
            connectDevice(camera);
        }
    }

    public void connectDevice(CameraRec camera) {
        camera.isLoad = 0;
        Runnable task = new ConnectTask(camera);
        taskQueue.addTask(task);
    }

    /**
     * @author harry
     */
    public class ConnectTask implements Runnable {
        private CameraRec camera;

        public ConnectTask(CameraRec camera) {
            this.camera = camera;
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(ToolUtil.LOGIN_NAME)) {
                connect(camera, ToolUtil.CONNECT_TYPE_LOCAL);
                return;
            }

            if (camera.key == 1) { // 平台
                connect(camera, ToolUtil.CONNECT_TYPE_P2P);
            } else { // 平台+本地
                connect(camera, ToolUtil.CONNECT_TYPE_LOCAL);
            }

        }
    }

    /**
     * 对指定的camera尝试联机
     * 
     * @param camera
     * @param conntype
     */
    public void connect(CameraRec camera, int conntype) {
        Log.d("harry", "*************connect");

        String service = camera.service;
        if (TextUtils.isEmpty(service)) {
            service = XmlRequstTool.DEFAULT_SERVICE;
        }
        camera.connty = spider.szc.JNI.NewcreateConn(service, camera.sn, camera.username,
                camera.password, conntype);
        camera.connType = conntype;

        Log.d("harry", "sn:" + camera.sn + " conn:" + camera.connty);
        Log.d("harry", " camera: " + camera);

        if (camera.connty > 10000) {
            camera.isLoad = 1;
            mainlist_camera_action.add(camera);
            getChannelNum(camera);
        }
        else if (camera.connty == ToolUtil.P2P_TIMEOUT1) {
            camera.connty = spider.szc.JNI.NewcreateConn(service, camera.sn, camera.username,
                    camera.password, ToolUtil.CONNECT_TYPE_RELAY);
            if (camera.connty > 10000) {
                camera.isLoad = 2;
                mainlist_camera_action.add(camera);
            } else {
                camera.isLoad = 5;
            }
        }
        else if (camera.connty == ToolUtil.P2P_OFFLINE) { // 105 7
            // 离线
            camera.isLoad = 5;
        } else if (camera.connty == ToolUtil.P2P_PASSERROR) { // 10
            // 密码错误
            camera.isLoad = 6;
        } else if (camera.connty == ToolUtil.CONNECT_TIMEOUT_1
                || camera.connty == ToolUtil.CONNECT_TIMEOUT_2) { // 1 2
            // 超时
            camera.isLoad = 7;
        } else if (camera.connty == 3) {
            // 未联机
            camera.isLoad = 4;
        } else if (camera.connType == ToolUtil.CONNECT_TYPE_RELAY && camera.connty < 10000) { // 3
            // 未联机
            camera.isLoad = 4;
        } else {
            connect(camera, ToolUtil.CONNECT_TYPE_RELAY); // 3
        }

        if (camera.isLoad == 1) {
            updateInfo(camera);
        }
        setAction(camera);
    }

    /**
     * @param camera
     */
    private void getChannelNum(final CameraRec camera) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                // 设备通道会变的，因此每次都要获取
                httpRequstTool.getBaseInfo(camera);
                if (camera.baseinfo != null) {
                    if (camera.baseinfo.chnum < 0 || camera.baseinfo.chnum > 32) {
                        camera.baseinfo.chnum = 1;
                    }
                    camera.channeNum = camera.baseinfo.chnum;
                    httpRequstTool.get_Reback(camera);
                }
            }
        });
    }

    /**
     * 设备服务信息上报
     * 
     * @param camera
     */
    private void updateInfo(final CameraRec camera) {
        if (TextUtils.isEmpty(ToolUtil.LOGIN_NAME)) {
            return;
        }
        if (camera.remoteID != null) {
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    int errcode = 11;
                    if (camera.connty > 10000) {
                        errcode = 0;
                    } else if (camera.connty == 7 || camera.connty == 10) {
                        errcode = camera.connty;
                    }
                    String request = getUpdateInfoRequest(ToolUtil.LOGIN_NAME, camera.remoteID,
                            camera.connType - 1, 0, errcode);
                    String response = reportToServer(request);
                    int result = analysisUpdateInfoResponse(response);
                }
            });
        }
    }

    /**
     * 生成设备服务信息请求（XML ）
     * 
     * @return
     */
    private String getUpdateInfoRequest(String userId, String remoteId, int linkType,
            int linkeTime, int errcode) {
        StringWriter writer = new StringWriter();

        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text("ServerInf");
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text("v1.0.1");
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserID");
            serializer.text(userId);
            serializer.endTag(null, "UserID");

            serializer.startTag(null, "Remote");
            serializer.attribute(null, "remoteID", remoteId);
            serializer.attribute(null, "LinkType", linkType + "");
            serializer.attribute(null, "LinkeTime ", linkeTime + "");
            serializer.attribute(null, "errcode", errcode + "");
            serializer.endTag(null, "Remote");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    /**
     * 向服务器发出请求
     * 
     * @param requestXml 请求数据
     * @return 响应数据
     */
    private String reportToServer(String requestXml) {
        String result = null;
        try {
            String url = "http://" + XmlRequstTool.URI + "/controlservlet";
            HttpPost httpRequest = new HttpPost(url);
            httpRequest.addHeader("Content-Type", "applcation/xml");
            StringEntity entity = new StringEntity(requestXml, "utf-8");
            httpRequest.setEntity(entity);

            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
            HttpConnectionParams.setSoTimeout(httpParams, 8000);
            HttpClient httpclient = new DefaultHttpClient(httpParams);

            HttpResponse response = httpclient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            InputStream inputStream = null;
            if (statusCode == HttpStatus.SC_OK) {
                inputStream = response.getEntity().getContent();
            } else {
                return null;
            }
            int len = 0;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            result = new String(outputStream.toByteArray(), "gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析服务器的响应（XML）
     * 
     * @param response 服务器的响应数据
     * @return
     */
    private int analysisUpdateInfoResponse(String response) {
        int result = -1;
        if (TextUtils.isEmpty(response)) {
            return result;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(response));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (event == XmlPullParser.START_TAG && "ErrorCode".equals(name)) {
                    String text = parser.nextText();
                    result = Integer.parseInt(text);
                    return result;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 将设备连接状况通知四屏和一屏以及设备管理
     */
    public void setAction(CameraRec camera) {
        // Intent intent = new Intent();
        // intent.setAction("action.refreshList");
        // intent.putExtra("connect_device", camera);
        // Log.d("harry", "send Broadcast: action.refreshList");
        // sendBroadcast(intent);
        // mHandler.sendEmptyMessage(START_AUTO_CONNECT);
        Log.d("harry", "要发广播了，但是没发 ");

    }

    /*
     * 关掉所有连接
     */
    private void destroyAllDevice() {
        taskQueue.stopLoop();
        taskQueue.deleteAllTask();
        for (int i = 0; i < cameraList.size(); i++) {
            CameraRec camera = cameraList.get(i);
            if (camera.connty > 10000)
                spider.szc.JNI.destroyConn(camera.connty);
        }
    }

    /***************************************************** 华丽的分割线 ***********************************************************/
    /***************************************************** 华丽的分割线 ***********************************************************/

    public static final int CONNECT_CONNECTINT = 0; // 正在联机
    private static final int CONNECT_BAR_GONE = 4;
    private static final int PHOTO_OK = 5;
    public static final int CONNECT_PASSWORD_ERROR = 10; // 密码错误
    private static final int START_RECORD_VIDEO = 11;
    private static final int STOP_RECORD_VIDEO = 12;
    private static final int BAR_VISIBLE = 15;
    private static final int BAR_GONE = 16;
    private static final int PTZIMG_GONE = 17;
    private static final int INSERT_EXTERNAL_STORAGE = 18;
    private static final int START_VIDEO_TEXT_VISIBLE = 19;
    private static final int START_VIDEO_TEXT_INVISIBLE = 20;
    private static final int RECORD_TIME_TEXT = 21;
    private static final int UPDATE_LIST = 23;
    private static final int pure_white_show = 24;
    private static final int pure_white_dismiss = 25;
    private static final int AFTER_STOP_VIDEO = 27;
    private static final int LIST_MOVE_LEFT = 28;
    private static final int DEVICE_LIST_GONE = 29;
    private static final int CONNECT_SUCCESS = 30;
    public static final int START_AUTO_CONNECT = 31;
    private static final int SOUND_BEFORE = 32;
    private static final int SOUND_AFTER = 33;
    private static final int BAR_WHICH_DO_WHAT = 34;
    private static final int ON_KEY_DOWN = 23456;

    private ArrayList<CameraRec> mCameraList;
    private ArrayList<CameraRec> camera_action_list_one;
    private TaskQueue_one queue_one;
    private CameraRec camRec_this;
    private CameraRec current_camera;
    private SurfaceHolder mSurfaceHolder1;

    private boolean isRead;// 判断是否观看成功
    private boolean isRecording;
    private int mConnecty;
    private boolean issound;
    private AudioTrack audioTrack;
    private boolean draw_black;
    private int stop_0;
    private Video video1;
    public static Bitmap bit_record_map;
    private Bitmap mBmp;
    private SharedPreferences auto_play_share_one;
    private boolean auto_play = true;
    private int auto_show_one;
    private boolean auto_connect_null_one;
    private boolean stop_auto;
    private long quick_click_time = 0;
    private boolean isRunning;
    private boolean start_check;
    private boolean can_connect_one = true;
    private long lastInComming = 0;
    private int connect_time;

    public Handler mHandler;

    enum ScreenState {
        STATE_ALL, STATE_FULL
    }

    // private void init() {
    // httpRequstTool = new XmlRequstTool();
    // cameraList = new ArrayList<CameraRec>();
    // mainlist_camera_action = new ArrayList<CameraRec>();
    // VideoQuality.initVideoQuality();
    // ToolUtil.setVideoStream(ToolUtil.VIDEO_PC_STREAM);
    // taskQueue.start();
    //
    // camera_action_list_one = mainlist_camera_action;
    // queue_one = new TaskQueue_one();
    // queue_one.start();
    // camRec_this = new CameraRec();
    // current_camera = new CameraRec();
    //
    // initData();
    //
    // initHandler();
    //
    // if (camera_action_list_one.size() == 0) {
    // auto_connect_null_one = true;
    // }
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // try {
    // Thread.sleep(1000);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // mHandler.sendMessage(mHandler.obtainMessage(START_AUTO_CONNECT));
    // }
    //
    // }).start();
    // }

    /**
     * init
     */
    private void readyToShowVideo() {
        Log.d("harry", "****************initMonitor");
        mCameraList = this.cameraList;

        camera_action_list_one = new ArrayList<CameraRec>();
        camera_action_list_one = this.mainlist_camera_action;
        queue_one = new TaskQueue_one();
        queue_one.start();
        camRec_this = new CameraRec();
        current_camera = new CameraRec();
        mSurfaceHolder1 = mSurfaceView.getHolder();
        auto_play_share_one = this.getSharedPreferences("auto_or_not_auto", 0);

        // IntentFilter intentFilter = new IntentFilter();
        // intentFilter.addAction("action.refreshList");
        // this.registerReceiver(mRefreshBroadcastReceiver, intentFilter);

        initHandler();

        if (camera_action_list_one.size() == 0) {
            auto_connect_null_one = true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendMessage(mHandler.obtainMessage(START_AUTO_CONNECT));
            }

        }).start();

    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        // 连接超时,观看失败
                        Toast.makeText(IpCameraTestActivity.this, R.string.watchfail,
                                Toast.LENGTH_SHORT)
                                .show();
                        if (isRecording) {
                            stop_video_must();
                        }

                        if (issound) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mConnecty > 10000) {
                                        mHandler.sendMessage(mHandler.obtainMessage(SOUND_BEFORE));
                                        if (audioTrack != null) {
                                            AudioTrack at = audioTrack;
                                            audioTrack = null;
                                            at.stop();
                                            at.release();
                                        }
                                        spider.szc.JNI.stopLiveAudio(mConnecty);
                                        issound = false;
                                    }
                                }
                            }).start();
                        }
                        // if (ptz_img.isShown()) {
                        // ptz_img.setVisibility(View.GONE);
                        // }
                        // bar1.setVisibility(View.GONE);
                        if (isRead) {
                            drawrgb();
                            isRead = false;
                        }
                        // if (moveleft) {
                        // listMoveRight();
                        // }
                        break;
                    case CONNECT_BAR_GONE:
                        // bar1.setVisibility(View.GONE);
                        // boundry1.setVisibility(View.GONE); 这一句导致了云台控制不管用了
                        // trans_full.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_OK:
                        Toast.makeText(IpCameraTestActivity.this, R.string.photook,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case START_RECORD_VIDEO:
                        Toast.makeText(IpCameraTestActivity.this, R.string.recordstart,
                                Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case STOP_RECORD_VIDEO:
                        Toast.makeText(IpCameraTestActivity.this, R.string.recordend,
                                Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 14:
                        Toast.makeText(IpCameraTestActivity.this, R.string.lookingNow, 500).show();
                        break;
                    case BAR_VISIBLE:
                        // bar1.setVisibility(View.VISIBLE);
                        break;
                    case BAR_GONE:
                        // bar1.setVisibility(View.GONE);
                        break;
                    case PTZIMG_GONE:
                        // ptz_img.setVisibility(View.GONE);
                        break;
                    case INSERT_EXTERNAL_STORAGE:
                        Toast.makeText(IpCameraTestActivity.this, R.string.external_storage,
                                Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case START_VIDEO_TEXT_VISIBLE:
                        // record_layout.setVisibility(View.VISIBLE);
                        break;
                    case START_VIDEO_TEXT_INVISIBLE:
                        // record_layout.setVisibility(View.INVISIBLE);
                        break;
                    case BAR_WHICH_DO_WHAT:
                        // ((View) msg.obj).setVisibility(msg.arg1);
                        break;
                    case UPDATE_LIST:
                        // adapter.notifyDataSetChanged();
                        break;
                    case pure_white_show:
                        // pure_white.setVisibility(View.VISIBLE);
                        break;
                    case pure_white_dismiss:
                        // pure_white.setVisibility(View.GONE);
                        break;
                    case AFTER_STOP_VIDEO:
                        if (!isRead) {
                            // bar1.setVisibility(View.GONE);
                            // boundry1.setVisibility(View.VISIBLE);
                            // stand_by.setVisibility(View.VISIBLE);
                            // ptz_img.setVisibility(View.GONE);
                            // if (devlist.isShown()) {
                            // devlist.setVisibility(View.GONE);
                            // }
                            // if (moveleft) {
                            // listMoveRight();
                            // }
                            stop_0 = 0;
                        }
                        break;
                    case LIST_MOVE_LEFT:
                        // if (moveright) {
                        // listMoveLeft();
                        // }
                        break;
                    case DEVICE_LIST_GONE:
                        // if (devlist.isShown()) {
                        // devlist.setVisibility(View.GONE);
                        // }
                        break;
                    case CONNECT_SUCCESS:
                        // if (bar1.isShown()) {
                        // bar1.setVisibility(View.GONE);
                        // trans_full.setVisibility(View.VISIBLE);
                        // }
                        // if(moveright){
                        // listMoveLeft();
                        // }
                        // if (devlist.isShown()) {
                        // devlist.setVisibility(View.GONE);
                        // }
                        video1.startCheckDataComming();
                        break;
                    case START_AUTO_CONNECT:
                        auto_connect();
                        break;
                    case SOUND_BEFORE:
                        // sound_one.setBackgroundResource(R.drawable.sound_xml);
                        break;
                    case SOUND_AFTER:
                        // sound_one.setBackgroundResource(R.drawable.sound_after_xml);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    public void auto_connect() {
        Log.d("harry", "****************auto_connect");
        if (auto_play_share_one != null) {
            auto_play = auto_play_share_one.getBoolean("auto_play_video", true);
        }

        if (auto_show_one < 1 && !auto_connect_null_one && !stop_auto && auto_play) {
            if (camera_action_list_one.size() > 0) {
                auto_connect_null_one = true;
                final CameraRec camera = camera_action_list_one.get(0);
                if (camera != null) {
                    if (camera.isLoad == 1 || camera.isLoad == 2 || camera.isLoad == 3) {

                        Log.i("harry", "auto---------------");

                        // stand_by.setVisibility(View.GONE);
                        // bar1.setVisibility(View.VISIBLE);
                        auto_show_one++;
                        queue_one.addTask(new Runnable() {
                            @Override
                            public void run() {
                                connect_device(camera, 0);
                            }
                        });
                        quick_click_time = 0;
                    }
                }
            }
        }
    }

    /**
     * @param camera
     * @param chn
     */
    public void connect_device(CameraRec camera, int chn) {
        Log.d("harry", "****************connect_device");
        int connType = 0;
        if (camera.key == 1) { // 平台
            connType = ToolUtil.CONNECT_TYPE_P2P;
        } else { // 平台+本地
            connType = ToolUtil.CONNECT_TYPE_LOCAL;
        }

        String service = camera.service;
        if (TextUtils.isEmpty(service)) {
            service = XmlRequstTool.DEFAULT_SERVICE;
        }

        for (int i = 0; i < 2; i++) {
            int err = 0;
            if (mConnecty == 1001 || i == 1)
                connType = ToolUtil.CONNECT_TYPE_RELAY;
            mConnecty = camera.connty;

            Log.d("harry", "。。。。mConnecty： " + mConnecty);

            if (mConnecty < 10000) {
                Log.d("harry", "。。。。又要调用JNI.NewcreateConn了！！！ ");
                mConnecty = spider.szc.JNI.NewcreateConn(service, camera.sn, camera.username,
                        camera.password, connType);
            }
            camera.connty = mConnecty;

            connect_time = i;

            if (mConnecty > 10000) {
                video1 = new Video(mSurfaceView.getWidth(), mSurfaceView.getHeight());
                // err =
                // spider.szc.JNI.startLiveVideo(conn,VideoQuality.getVideoQuality(camera),ToolUtil.VIDEO_MAX_STREAM,
                // video1, -1);
                Log.d("harry", "。。。。開始要現場直播了！！");
                err = spider.szc.JNI.startLiveVideo(mConnecty,
                        VideoQuality.getVideoQuality(camera),
                        ToolUtil.VIDEO_MAX_STREAM, chn, video1);
                Log.d("harry", "****************connect_device err:" + err);

                if (err == 0) {
                    camRec_this = camera;
                    isRead = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ToolUtil.saveImage(camera, bit_record_map);
                    mHandler.sendMessage(mHandler.obtainMessage(UPDATE_LIST));
                    return;
                } else {
                    if (connect_time == 1) {
                        spider.szc.JNI.destroyConn(mConnecty);
                        mConnecty = 0;
                        camRec_this.connty = 0;
                    }
                }
            }
        }
        Message boun = mHandler.obtainMessage();
        boun.what = 1;
        mHandler.sendMessage(boun);
    }

    /**
     * 任务队列
     */
    private class TaskQueue_one extends Thread {
        private LinkedBlockingQueue<Runnable> queue_one;
        private boolean isLoop = true;

        public TaskQueue_one() {
            queue_one = new LinkedBlockingQueue<Runnable>();
        }

        public void addTask(Runnable task) {
            queue_one.offer(task);
        }

        public void stopLoop() {
            queue_one.offer(new Runnable() {
                @Override
                public void run() {
                    isLoop = false;
                }
            });
        }

        @Override
        public void run() {
            while (isLoop) {
                try {
                    Runnable task = queue_one.take();
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * stop_video_must
     */
    public void stop_video_must() {
        isRecording = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mConnecty > 10000) {
                    spider.szc.JNI.stopRecord(mConnecty);
                }
                mHandler.sendMessage(mHandler.obtainMessage(START_VIDEO_TEXT_INVISIBLE));
            }
        }).start();

    }

    /**
     * drawrgb
     */
    public void drawrgb() {
        SurfaceHolder holder = mSurfaceView.getHolder();
        Canvas canvas = mSurfaceHolder1.lockCanvas(null);// 获取画布
        canvas.drawRGB(0, 0, 0);
        holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        draw_black = true;
    }

    /**
     * @author harry
     */
    class Video implements ColorArray {
        private int mPicWidth, mPicHeight;
        private int mSurfaceHeight;
        private int mSurfaceWidth;
        private int[] mColors;
        private int mInvalidCnt;
        private float mTgtX, mTgtY, mTgtW, mTgtH;
        private byte[] audioCache;

        private boolean mbScale = true;
        protected boolean mIsDimensonChanged;

        private IntBuffer mBmpBuffer;

        private RectF mDstR;
        private RectF mNewDst = new RectF();

        protected ScreenState mScreenState = ScreenState.STATE_ALL;

        public Video(int mSurfaceWidth, int mSurfaceHeight) {
            this.mSurfaceHeight = mSurfaceHeight;
            this.mSurfaceWidth = mSurfaceWidth;
        }

        public void setScreenState(ScreenState state, int mSurfaceWidth, int mSurfaceHeight) {
            this.mScreenState = state;
            this.mSurfaceHeight = mSurfaceHeight;
            this.mSurfaceWidth = mSurfaceWidth;
        }

        // 第一帧视频来的时候调用，仅调用一次
        @Override
        public boolean dimensionChanged(int width, int height) {
            mPicWidth = width;
            mPicHeight = height;
            mBmp = null;
            mColors = null;
            mColors = new int[width * height];
            mBmpBuffer = IntBuffer.wrap(mColors);//
            mBmp = Bitmap.createBitmap(mPicWidth, mPicHeight, Bitmap.Config.RGB_565);
            mInvalidCnt++;
            mIsDimensonChanged = true;// add by xjk
            refreshPosition();// add by xjk
            mHandler.sendMessage(mHandler.obtainMessage(CONNECT_SUCCESS));

            return mColors != null;
        }

        public void startCheckDataComming() {
            isRunning = true;
            lastInComming = System.currentTimeMillis();
            check = new Thread() {
                public void run() {
                    while (isRunning) {
                        long time = System.currentTimeMillis() - lastInComming;
                        if (time > 10000) {
                            noDataComming();
                            return;
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            check.start();
            start_check = true;
        }

        public void stopCheckDataComming() {
            isRunning = false;
            check.interrupt();
            start_check = false;
        }

        private Thread check = new Thread() {
            public void run() {
                while (isRunning) {
                    long time = System.currentTimeMillis() - lastInComming;
                    if (time > 10000) {
                        noDataComming();
                        return;
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                check.interrupt();
            }

        };

        // 第一帧视频来的时候调用，仅调用一次
        @Override
        public int[] getColorBuffer() {
            return mColors;
        }

        // 每帧视频来的时候都会调用
        @Override
        public void frameReady() {
            Log.d("harry", "**************************frameReady..............");
            lastInComming = System.currentTimeMillis();

            Canvas canvas = mSurfaceHolder1.lockCanvas();
            if (canvas != null) {
                if (mInvalidCnt > 1)
                    canvas.drawRGB(0, 0, 0);
                mBmpBuffer.rewind();
                mBmp.copyPixelsFromBuffer(mBmpBuffer);
                canvas.clipRect(mDstR.left, mDstR.top, mDstR.right, mDstR.bottom);
                canvas.drawRGB(0, 0, 0);
                bit_record_map = mBmp;
                if (mScreenState == ScreenState.STATE_FULL) {
                    canvas.save();
                    mNewDst.set(mDstR.left, mDstR.top, mDstR.right, mDstR.bottom);
                    canvas.drawBitmap(mBmp, null, mNewDst, null);
                    canvas.restore();
                } else {
                    canvas.drawBitmap(mBmp, null, mDstR, null);
                }
                mSurfaceHolder1.unlockCanvasAndPost(canvas);
            }
        }

        @Override
        public byte[] allocateAudioBuffer() {
            int size = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            audioCache = new byte[size];
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, size,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            return audioCache;
        }

        @Override
        public void audioReady(int len) {
            if (audioTrack != null) {
                audioTrack.write(audioCache, 0, len);
            }
        }

        @Override
        public void onProgress(int thousandth, int milliseconds) {

        }

        protected void refreshPosition() {
            if (!mIsDimensonChanged) {
                return;
            }
            int w, h;
            w = mSurfaceWidth;
            h = mSurfaceHeight;

            if (mbScale) {
                float xRatio, yRatio;
                xRatio = w / (mPicWidth * 1.0f);
                yRatio = h / (mPicHeight * 1.0f);

                if (xRatio < yRatio) {
                    mTgtX = 0;
                    mTgtW = w;
                    mTgtH = mSurfaceHeight;
                    mTgtY = (h - mTgtH) / 1.0f;
                } else {
                    mTgtY = 0;
                    mTgtH = h;
                    mTgtW = mSurfaceWidth;
                    mTgtX = (w - mTgtW) / 1.0f;
                }
            } else {
                mTgtW = (w > mPicWidth) ? mPicWidth : w;
                mTgtH = (h > mPicHeight) ? mPicHeight : h;
                mTgtX = (w - mTgtW) / 2.0f;
                mTgtY = (h - mTgtH) / 2.0f;
            }
            if (mDstR == null) {
                mDstR = new RectF();
            }
            mDstR.left = mTgtX;
            mDstR.top = mTgtY;
            mDstR.right = (mTgtX + mTgtW);
            mDstR.bottom = (mTgtY + mTgtH);
        }
    }

    private void noDataComming() {
        Log.i("i", "没有数据到来，重新连接");
        // handler.sendMessage(handler.obtainMessage(BAR_VISIBLE));
        if (isRecording) {
            stop_video_must();
        }
        isRead = false;
        if (mConnecty > 10000) {
            if (video1 != null && start_check) {
                video1.stopCheckDataComming();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    spider.szc.JNI.stopLiveVideo(mConnecty);
                    // spider.szc.JNI.destroyConn(conn);
                    mConnecty = 0;
                    camRec_this.connty = 0;

                    mHandler.sendMessage(mHandler.obtainMessage(BAR_VISIBLE));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (can_connect_one) {
                        connect_device(camRec_this, 0);
                    }
                    if (isRead) {
                        mHandler.sendMessage(mHandler.obtainMessage(CONNECT_SUCCESS));
                    }
                }
            }).start();
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy........................");

        /*************************************************************/
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                destroyAllDevice();
            }
        });
        if (mainlist_camera_action.size() != 0) {
            mainlist_camera_action.clear();
        }
        /*************************************************************/

        can_connect_one = false;
        if (isRead) {
            new ProgressAsynTask<Void, Void, Void>(this, R.string.shut_video) {
                @Override
                protected Void doInBackground(Void... params) {
                    if (video1 != null && start_check) {
                        video1.stopCheckDataComming();
                    }
                    ToolUtil.saveImage(camRec_this, bit_record_map);
                    if (mConnecty > 10000) {
                        spider.szc.JNI.stopLiveVideo(mConnecty);
                        // spider.szc.JNI.destroyConn(conn);
                        // conn = 0;
                        // if(camRec_this != null){
                        // camRec_this.connty = 0;
                        // }
                        isRead = false;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    // finish();
                    // overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    super.onPostExecute(result);
                }
            }.execute();
        } else {
            // finish();
            // overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
        if (queue_one != null)
            queue_one.stopLoop();
        // this.unregisterReceiver(mRefreshBroadcastReceiver);
        super.onDestroy();
        System.gc();
    }
    /***************************************************** 华丽的分割线 ***********************************************************/

}
