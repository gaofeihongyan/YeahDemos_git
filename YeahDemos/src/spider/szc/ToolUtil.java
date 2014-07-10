
package spider.szc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ToolUtil {
    /**
     * �豸�б����õ��Ĳ���
     */
    public static String user_root_dir;
    public static String user_cloud_dir;

    /**
     * ���ݴ洢����
     */
    public static boolean isStorageDirExit;
    public static String SAVE_LOGIN = "danalelogin";
    public static String SAVE_LOGIN_NAME = "danaleusername";
    public static String SAVE_LOGIN_PASSWORD = "danalepassword";
    public static String VIDEO_IMAGE_NAME = "/Danale_VideoImage/";// add
                                                                  // 2013.12.07
    public static final String DEVICE_SERVICE = "svrshp.thingsnic.com";
    public static String IPTV_FILE;// add 2014.01.11
    public static String IPTV_DEF_FILE = Environment.getExternalStorageDirectory() + "/Danale_Iptv";
    public static final String PLATFORM_SERVICE = "thingsnic.com";
    public static final String REQUEST_VERSION = "1.0";
    public static String SET_SERVICE = "2";// p2p�������

    public static final int REQUEST_TIMEOUT = 8 * 1000; // ����ʱ
    public static final int SO_TIMEOUT = 8 * 1000; // ���ӳ�ʱ

    // ��������
    public static final int CONNECT_TYPE_LOCAL = 1; // LOCAL
    public static final int CONNECT_TYPE_P2P = 2; // P2P
    public static final int CONNECT_TYPE_RELAY = 3; // RELAY

    /**
     * ��������
     */
    public static final int CONNECT_TIMEOUT_1 = 1; // ��ʱ
    public static final int CONNECT_TIMEOUT_2 = 2; // ��ʱ
    public static final int P2P_OFFLINE = 7; // ����
    public static final int P2P_TIMEOUT1 = 105; // ����
    public static final int P2P_PASSERROR = 10; // �������
    public static final int CONNECT_RELAY_1 = 555; // RELAY
    public static final int CONNECT_RELAY_2 = 1001; // RELAY
    public static final int CONNECT_NORMAL = 10000; // ���ڸ�ֵ������

    // �豸��Դ
    public static final int ONLINE_TYPE_OFFLINE = 0; // ����
    public static final int ONLINE_TYPE_PLATFORM = 1; // ƽ̨��ȡ
    public static final int ONLINE_TYPE_NATIVE = 2; // ���ػ�ȡ
    public static final int ONLINE_TYPE_PLATFORM_NATIVE = 3; // ƽ̨����ͬʱ��ȡ

    // �豸����
    public static final int DEV_TYPE_IPC = 0;
    public static final int DEV_TYPE_DVR = 1;

    /**
     * ��Ƶ��������
     */
    public static final int VIDEO_PC_STREAM = 0; // IPTV
    public static final int VIDEO_MAX_STREAM = 1; // ���壨��һ�������ߣ�
    public static final int VIDEO_MIDDLE_STREAM = 2; // ���壨�ڶ��������У�
    public static final int VIDEO_MIN_STREAM = 3; // ���壨�����������ͣ�
    public static int videoStream = 0;

    // ��Ƶ����
    public static final int VIDEO_QUALITY_LOWEST = 0; // ���
    public static final int VIDEO_QUALITY_LOW = 1; // ��
    public static final int VIDEO_QUALITY_NORMAL = 2; // ��
    public static final int VIDEO_QUALITY_HIGH = 3; // ��
    public static final int VIDEO_QUALITY_HIGHEST = 4; // ���
    public static final int VIDEO_QUALITY_AUTO = 101; // �Զ�

    // �������������
    public static final String REQUEST_TYPE_LOGIN = "UserLoginReq";
    public static final String REQUEST_TYPE_REGIST = "UserRegReq";
    public static final String REQUEST_TYPE_FIND = "UseFindPwdReq";
    public static final String REQUEST_TYPE_ADD_DEVICE = "PostActiveServerReq";
    public static final String REQUEST_TYPE_DELETE_DEVICE = "PostDelRemoteReq";
    public static final String REQUEST_TYPE_MODIFY_DEVICE = "PostMfyRemoteReq";
    public static final String REQUEST_TYPE_GET_CAMERA = "PostRemoteInfoReq";

    // Ƶ��
    public static final int FREQUENCY_ERROR = -1;
    public static final int FREQUENCY_50HZ = 0;
    public static final int FREQUENCY_60HZ = 1;

    // ��̨����ָ��
    public static final int PTZ_STOP = 100;
    public static final int PTZ_UP = 101;
    public static final int PTZ_DOWN = 103;
    public static final int PTZ_LEFT = 105;
    public static final int PTZ_RIGHT = 107;
    public static final int PTZ_LIGHT_CIRCLE_NEER = 117;
    public static final int PTZ_LIGHT_CIRCLE_FAR = 119;
    public static final int PTZ_ZOOM_FAR = 121;
    public static final int PTZ_ZOOM_NEER = 123;
    public static final int PTZ_ZOOM_IN = 125;
    public static final int PTZ_ZOOM_OUT = 127;

    // ��Ļ����
    public static final int ORINTATION_NORMAL = 0;
    public static final int ORINTATION_HORIZONTAL = 1;
    public static final int ORINTATION_VERTICAL = 2;
    public static final int ORINTATION_ALL = 3;

    /**
     * wifi�����еĲ���
     */
    public static String TYPE = null;
    public static String USERID = null;
    public static String SERVER = null;
    public static String ISMODIFY = null;
    public static CameraRec ca;
    /**
     * ɫ�������еĲ���
     */
    public static int MAX_BRIGHTNESS = 100;// 256;
    public static int MAX_CONTRAST = 100;// 256;
    public static int DEFAULT_SATURATION = 50;// 128;
    public static int DEFAULT_HUE = 50;// 128;

    /**
     * ��¼��Ϣ
     */
    public static String LOGIN_NAME = "";// ��¼�� null Ϊ��
    public static String KEY;// 0ƽ̨��¼ 1���ص�¼
    public static String LOGIN_PASSWORD;

    public static String LOCAL_PHOTO = "local_photo";
    public static String LOCAL_VIDEO = "local_video";
    /**
     * �ļ���
     */
    public static String REMOST_IMAGE_NAME = "/DanaleImage/";
    public static String PHOTONAME = "danalePhoto";
    public static String FILENAME = "danaleRecord";
    public static String CLOUD_IMAGE_NAME = "/CloudImage/";
    public static String PHOTO_IMAGE_NAME = "/Danale_IPTV_Photo/";// add
                                                                  // 2013.12.30
    public static boolean sdCardExist = android.os.Environment.getExternalStorageState().equals(
            android.os.Environment.MEDIA_MOUNTED);
    // Ĭ���˺�����
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin";

    /**
     * ���캯��
     */
    public ToolUtil(String s1) {
        LOGIN_NAME = s1;
    }

    /**
     * ����so������ʼ��
     */
    public static void loadLib() {
        // System.loadLibrary("cam");
        Log.d("harry", "loadLib....");
        System.loadLibrary("p2pclt");
        // spider.szc.JNI.init(XmlRequstTool.SERVICE3, "");
        // com.example.iptv.JNI.init();
    }

    /**
     * ���Ⱪ¶��JNI��ʼ����ɵĽӿ�
     */
    public static interface OnJniInitCompleteListener {
        public void onComplete(boolean compelte);
    }

    private static OnJniInitCompleteListener listener;
    private static boolean jniInitState;

    public static boolean setOnJniInitCompleteListener(OnJniInitCompleteListener listener) {
        synchronized (ToolUtil.class) {
            if (jniInitState == false) {
                ToolUtil.listener = listener;
            }
            return jniInitState;
        }
    }

    /**
     * ��ʼ��JNI
     */
    public static void initso() {
        new Thread() {
            public void run() {
                jniInitState = false;
                JNI.init(XmlRequstTool.SERVICE3, "");

                synchronized (ToolUtil.class) {
                    jniInitState = true;
                    if (listener != null) {
                        listener.onComplete(jniInitState);
                        listener = null;
                    }
                }
            }
        }.start();
    }

    /**
     * �����ʼ��
     */
    public static void uninit() {
        JNI.uninit();
    }

    /**
     * �������ͼ��
     */
    public static boolean getNetType(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable()) {
            return true;
        } else if (con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isAvailable()) {
            return false;
        }
        return false;
    }

    /**
     * ����ͼƬ����
     * 
     * @param sn
     * @return
     */
    public static Bitmap getImageCache(String sn) {
        String path = getUserRootDir();
        if (path == null) {
            return null;
        }
        String new_path = path + sn + ".jpg";
        File file = new File(new_path);

        if (file.exists() && file.isFile()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * ����onvifͼƬ����
     * 
     * @param sn
     * @return
     */
    public static Bitmap getOnvifImage(String ip) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/"
                + "Danale_Onvif_Frame/";
        if (path == null) {
            return null;
        }
        String new_path = path + ip + ".jpg";
        File file = new File(new_path);
        if (file.exists() && file.isFile()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * ����ͼƬ����
     * 
     * @return /mnt/sdcard/Login_name/VideoImage/
     */
    public static Bitmap getVideoCache(String video_photo, String video_name, String time_name) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        if (!time_name.endsWith("jpg")) {
            time_name = time_name + ".jpg";
        }

        String user_video_dir = Environment.getExternalStorageDirectory() + "/"
                + ToolUtil.LOGIN_NAME +
                video_photo + video_name + "/" + time_name;
        File file = new File(user_video_dir);
        if (file.exists() && file.isFile()) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 8;
            return BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

        }
        return null;
    }

    /**
     * ��ȡ�û���Ŀ¼
     * 
     * @return /mnt/sdcard/Login_name/Danale_Image/
     */
    public static String getUserRootDir() {

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        if (user_root_dir != null) {
            return user_root_dir;
        }

        // StringBuilder builder = new
        // StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        user_root_dir = Environment.getExternalStorageDirectory() + "/" + ToolUtil.LOGIN_NAME
                + ToolUtil.REMOST_IMAGE_NAME;
        return user_root_dir;
    }

    /**
     * ��ȡ�û���Ŀ¼
     * 
     * @return /mnt/sdcard/Login_name/CloudImage/
     */
    public static String getUserCloudDir() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        if (user_cloud_dir != null) {
            return user_cloud_dir;
        }
        user_cloud_dir = Environment.getExternalStorageDirectory() + "/" + ToolUtil.LOGIN_NAME
                + ToolUtil.CLOUD_IMAGE_NAME;
        return user_cloud_dir;
    }

    public static void setVideoStream(int stream) {
        ToolUtil.videoStream = stream;
    }

    public static ArrayList<String> checkStorage() {
        ArrayList<String> storage_list = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            storage_list.clear();
            while ((line = br.readLine()) != null) {
                if (line.contains("vfat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1 && columns[1].contains("mnt")) {
                        storage_list.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storage_list;
    }

    public static boolean checkDirIsExit(ArrayList<String> allArray, String dir) {
        if (dir.equals(ToolUtil.IPTV_DEF_FILE))
            return true;
        if (allArray.size() == 0)
            return false;
        File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.isDirectory())
            return true;
        return false;
    }

    public static boolean getIptvDir() {
        if (!sdCardExist) {
            return false;
        }
        boolean mkbool;
        IPTV_FILE = IPTV_DEF_FILE;
        File file = new File(IPTV_FILE);
        if (!file.exists()) {
            mkbool = file.mkdirs();
        } else {
            mkbool = true;
        }
        return mkbool;
    }

    // �����豸
    public static boolean connectDevice(CameraRec camera) {
        if (camera == null)
            return false;

        if (TextUtils.isEmpty(camera.service)) {
            camera.service = XmlRequstTool.DEFAULT_SERVICE;
        }
        for (int i = 0; i < 2; i++) {
            camera.connty = JNI.NewcreateConn(camera.service, camera.sn, camera.username,
                    camera.password, camera.connType);
            if (camera.connty > 10000) {
                return true;
            }
        }

        camera.connty = 0;
        return false;
    }

    public static void destroyConnect(CameraRec camera) {
        if (camera == null)
            return;

        if (camera.connty > 10000) {
            JNI.destroyConn(camera.connty);
        }
        camera.connty = 0;
    }

    // ����ʱ����ͼƬ
    public static void saveImage(CameraRec came, Bitmap bitmap) {
        boolean mkbool;
        if (bitmap == null)
            return;
        int dstWidth = bitmap.getWidth();
        int dstHeight = bitmap.getHeight();
        if (dstWidth >= 250 || dstHeight >= 250) {
            dstWidth >>= 1;
            dstHeight >>= 1;
        }

        File external_storage = Environment.getExternalStorageDirectory();
        String path = external_storage.getPath() + "/" + ToolUtil.LOGIN_NAME;
        File file_one = new File(path);
        if (!file_one.exists()) {
            file_one.mkdirs();
        }

        String get_bitmap = file_one.getPath() + ToolUtil.REMOST_IMAGE_NAME;
        File file = new File(get_bitmap);
        if (!file.exists()) {
            mkbool = file.mkdirs();
        } else {
            mkbool = true;
        }

        try {
            FileOutputStream out = null;
            if (mkbool) {
                out = new FileOutputStream(get_bitmap + came.sn + ".jpg");
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
                bitmap.compress(CompressFormat.JPEG, 75, out);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
