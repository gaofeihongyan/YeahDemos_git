
package spider.szc;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import spider.szc.JNI.Device;
import spider.szc.JNI.onvDevice;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类---xml请求
 */
public class XmlRequstTool {

    public String result;
    public String result1;
    public String result_qrcode;
    public String result_qr;
    // public String login_result;
    public static final String URI = "www.danale.com";// thingsnic 平台
    public static String SERVICE = "113.106.88.198";// thingsnic服务器
    public static String SERVICE1 = "202.181.198.100";// danale 服务器1
    public static String SERVICE2 = "202.181.198.101";// danale 服务器2
    public static String SERVICE3 = "ss1.danale.com";// danale
    public static String DEFAULT_SERVICE = "sp1.danale.com";// danale
    public static final String QR_Code_URI = "http://pushsvr.danale.com/IPTVControlServlet";// QRCode
                                                                                            // 平台
    public static final String GET_QR = "GetQR";
    public static final String QR_VERSION = "1.0.1";
    public static final String LOGIN_VERSION = "1.0";
    public static final String LOGIN_URL = "http://www.danale.com/controlservlet";
    public static final String MESSAGE_NOTICE_URL = "http://pushsvr.danale.com/controlServlet"; // 消息通知平台
    public static final String REQUEST_TYPE_ADD_DEVICE = "PostActiveServerReq";
    // public static final String REQUEST_TYPE_DELETE_DEVICE =
    // "PostDelRemoteReq"; //设备管理删除设备
    public static final String REQUEST_TYPE_DELETE_DEVICE = "DADelRemoteReq"; // 设备管理删除设备
    public static final String REQUEST_TYPE_DEVICE_CLOUD = "GetDevicesStorageType"; // 设备信息
                                                                                    // 存储设置
    public static final String REQUEST_DEVICE_CLOUD_MESSAGE = "GetStorageServices"; // 设备信息
                                                                                    // 存储设置
    public static final String REQUEST_DEVICE_CLOUD_OPEN = "SetStorageServices"; // 设备信息
                                                                                 // 存储设置
    public static final String REQUEST_MESSAGE_NOTICE_OPEN = "ClientGetPushInfo"; // 消息通知是否开启
    public static final String REQUEST_MESSAGE_NOTICE_SET = "ClientSetPushInfo"; // 设置消息通知

    /**
     * 用户验证
     */
    public String user_login_check(String user_name, String password) {
        String login_send_message = send_server_message(user_name, password);
        String login_response = reportToServer(login_send_message, LOGIN_URL);

        String login_response_result = analysisLoginResponse(login_response);
        return login_response_result;
    }

    public String send_server_message(String login_user, String login_pass) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(ToolUtil.REQUEST_TYPE_LOGIN);
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(LOGIN_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserName");
            serializer.text(login_user);
            serializer.endTag(null, "UserName");

            serializer.startTag(null, "UserPwd");
            serializer.text(login_pass);
            serializer.endTag(null, "UserPwd");

            serializer.startTag(null, "SystemDomain");
            serializer.text("");
            serializer.endTag(null, "SystemDomain");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private String analysisLoginResponse(String response) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(response));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if ("Result".equals(name)) {
                        String login_result = parser.nextText();
                        return login_result;
                    }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改设备密码 REQUEST_TYPE_MODIFY_DEVICE = "PostMfyRemoteReq";
     */
    public String get_Reback(CameraRec c) {
        String camera_message = modify_camera_name_pass(c);
        String camera_report_server = reportToServer(camera_message, LOGIN_URL);
        String camera_response = analysisLoginResponse(camera_report_server);
        return camera_response;
    }

    public String modify_camera_name_pass(CameraRec camera) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(ToolUtil.REQUEST_TYPE_MODIFY_DEVICE);
            serializer.endTag(null, "MsgType");
            serializer.startTag(null, "Version");
            serializer.text(LOGIN_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserID");
            serializer.text(ToolUtil.LOGIN_NAME);
            serializer.endTag(null, "UserID");
            serializer.startTag(null, "RemoteID");
            if (camera.remoteID != null) {
                serializer.text(camera.remoteID);
            }
            serializer.endTag(null, "RemoteID");

            serializer.startTag(null, "ViewUser");
            serializer.text(camera.username);
            serializer.endTag(null, "ViewUser");

            serializer.startTag(null, "ViewUserPwd");
            serializer.text(camera.password);
            serializer.endTag(null, "ViewUserPwd");
            serializer.startTag(null, "RemoteName");
            serializer.text(camera.name);
            serializer.endTag(null, "RemoteName");

            serializer.startTag(null, "SystemDomain");
            serializer.text("");
            serializer.endTag(null, "SystemDomain");
            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public String send_QRCode_ToServer(String uuid) {
        String request_qrcode = send_request_qrcode(uuid);
        String response_qrcode = reportToServer(request_qrcode, QR_Code_URI);
        String QR_result = analysisQRResponse(response_qrcode);
        return QR_result;
    }

    public String send_request_qrcode(String uuid) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(GET_QR);
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(QR_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "Guid");
            serializer.text(uuid);
            serializer.endTag(null, "Guid");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    private String analysisQRResponse(String response) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(response));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if ("Result".equals(name)) {
                        result_qr = parser.nextText();
                        return result_qr;
                    }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_qr;
    }

    /**
     * 获取平台列表数据
     * 
     * @return
     */
    public List<CameraRec> getAllDevice() {
        List<CameraRec> platformCameraList = null;
        List<CameraRec> localList = getLanDevices();
        List<CameraRec> local_and_platform_device = new ArrayList<CameraRec>();
        Map<String, CameraRec> map = new HashMap<String, CameraRec>();

        if (TextUtils.isEmpty(ToolUtil.LOGIN_NAME) || TextUtils.isEmpty(ToolUtil.LOGIN_PASSWORD)) {
            if (localList != null)
                return localList;
            return null;
        }
        // if(ToolUtil.KEY=="1" && ToolUtil.LOGIN_NAME=="" &&
        // ToolUtil.LOGIN_PASSWORD==""){
        // if(localList!=null)
        // return localList;
        // return null;
        // }

        platformCameraList = getPlatformCamera(ToolUtil.LOGIN_NAME);

        Log.i("iii", "platformCameraList--------------" + platformCameraList.size());

        if (localList != null) {
            for (int i = 0; i < localList.size(); i++) {
                CameraRec camera = localList.get(i);
                map.put(camera.getSn(), camera);
            }
        }
        for (CameraRec camera : platformCameraList) {
            // 本地+平台数据
            if (map.containsKey(camera.getSn())) {
                CameraRec localCamera = map.get(camera.getSn());
                camera.setHost(localCamera.getHost());
                camera.setPort(localCamera.getPort());
                camera.setConnType(localCamera.getConnType());
                camera.setOnlineType(ToolUtil.ONLINE_TYPE_PLATFORM_NATIVE);
                camera.setService(localCamera.getService());
                camera.setSn(localCamera.getSn());
                camera.setMac(localCamera.getMac());
                camera.setNetMask(localCamera.getNetMask());
                camera.setMdns(localCamera.getMdns());
                camera.setSdns(localCamera.getSdns());
                camera.setDomainName(localCamera.getDomainName());
                camera.setDdnsuser(localCamera.getDdnsuser());
                camera.setDdnsp(localCamera.getDdnsp());
                camera.setDdn(localCamera.getDdn());
                camera.setGw(localCamera.getGw());
                camera.setVersion(localCamera.getVersion());
                camera.setDdnsp(localCamera.getDdnsp());
                camera.setChanneName(localCamera.getChanneName());
                camera.setChanneNum(localCamera.getChanneNum());
                camera.setUdpPort(localCamera.getUdpPort());
                camera.key = 3;// 本地、平台都有

            } else {
                // 平台数据
                camera.setConnType(ToolUtil.CONNECT_TYPE_P2P);
                camera.setOnlineType(ToolUtil.ONLINE_TYPE_PLATFORM);
            }
            local_and_platform_device.add(camera);
        }

        return local_and_platform_device;
    }

    // 获取局域网的设备
    public static List<CameraRec> getLanDevices() {
        Log.d("harry","...获取局域网的设备");
        List<CameraRec> deviceList = new ArrayList<CameraRec>();
        Device[] devices = JNI.enumDevices();
        if (devices == null) {
            return deviceList;
        }
        for (Device device : devices) {
            CameraRec camera = new CameraRec();
            camera.setName(device.name);
            camera.host = device.ip;
            camera.port = device.port;
            camera.username = ToolUtil.DEFAULT_USERNAME;
            camera.password = ToolUtil.DEFAULT_PASSWORD;
            camera.connType = ToolUtil.CONNECT_TYPE_LOCAL;
            camera.onlineType = ToolUtil.ONLINE_TYPE_NATIVE;
            camera.service = ToolUtil.DEVICE_SERVICE;
            camera.sn = device.sn;
            camera.mac = device.mac;
            camera.netMask = device.netmask;
            camera.mainDns = device.mdns;
            camera.seconDns = device.sdns;
            camera.domainName = device.dDomainname;
            camera.ddnsuser = device.ddnsuser;
            camera.ddnsp = device.ddnsp;
            camera.ddn = device.ddn;
            camera.gw = device.gw;
            camera.key = 2; // 本地
            camera.setDevType(ToolUtil.DEV_TYPE_IPC);
            camera.channeNum = 1;
            camera.version = device.version;
            camera.dhcp = device.dhcpen;
            camera.channeName = device.dChanneName;
            try {
                camera.udpPort = Integer.parseInt(device.dUdpPort);
            } catch (Exception e) {
                camera.udpPort = 0;
            }
            deviceList.add(camera);
        }
        return deviceList;
    }

    /**
     * 获取平台列表
     * 
     * @param username 用户名
     * @return
     */
    public static List<CameraRec> getPlatformCamera(String username) {
        String request = getPlatformCameraRequest(username);
        String response = reportToServer(request, LOGIN_URL);

        List<CameraRec> list = analysisPlatformCameraResponse(response);
        return list;
    }

    /**
     * 生成获取平台列表请求（XML）
     * 
     * @param username 登录用户名
     * @return
     */
    private static String getPlatformCameraRequest(String username) {
        if (username == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(ToolUtil.REQUEST_TYPE_GET_CAMERA);
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(ToolUtil.REQUEST_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserID");
            serializer.text(username);
            serializer.endTag(null, "UserID");

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
    public static String reportToServer(String requestXml, String URL) {
        String result = null;
        try {
            HttpPost httpRequest = new HttpPost(URL);
            httpRequest.addHeader("Content-Type", "applcation/xml");
            StringEntity entity = new StringEntity(requestXml, "utf-8");
            httpRequest.setEntity(entity);

            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, ToolUtil.REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, ToolUtil.SO_TIMEOUT);
            HttpClient httpclient = new DefaultHttpClient(httpParams);

            HttpResponse response = httpclient.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                Log.e("http", "StatusCode:" + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析平台返回的响应数据（XML）
     * 
     * @param response
     * @return
     */
    private static List<CameraRec> analysisPlatformCameraResponse(String response) {
        List<CameraRec> list = new ArrayList<CameraRec>();
        if (TextUtils.isEmpty(response)) {
            return list;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(response));
            int event = parser.getEventType();
            CameraRec camera = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if ("Remote".equals(name)) {
                        camera = new CameraRec();
                        camera.setRemoteID(parser.getAttributeValue(null, "RemoteID"));
                        camera.setUsername(parser.getAttributeValue(null, "ViewUser"));
                        camera.setPassword(parser.getAttributeValue(null, "ViewUserPwd"));
                        try {
                            camera.setPort(Integer.parseInt(parser.getAttributeValue(null,
                                    "TCPPort")));
                        } catch (Exception e) {
                            camera.setPort(0);
                        }
                        try {
                            camera.setUdpPort(Integer.parseInt(parser.getAttributeValue(null,
                                    "UDPPort")));
                        } catch (Exception e) {
                            camera.setUdpPort(0);
                        }
                        camera.setName(parser.getAttributeValue(null, "RemoteName"));
                        camera.setHost(parser.getAttributeValue(null, "DomainInfo"));
                        camera.setDomainName(parser.getAttributeValue(null, "Domainname"));
                        camera.setSn(parser.getAttributeValue(null, "SN"));
                        camera.setKey(1); // 平台
                        try {
                            camera.setOnlineType(Integer.parseInt(parser.getAttributeValue(null,
                                    "Online")));
                        } catch (Exception e) {
                            camera.setOnlineType(ToolUtil.ONLINE_TYPE_OFFLINE);
                        }
                        try {
                            camera.setDevType(Integer.parseInt(parser.getAttributeValue(null,
                                    "DevType")));
                        } catch (Exception e) {
                            camera.setDevType(ToolUtil.DEV_TYPE_IPC);
                        }
                        camera.setService(parser.getAttributeValue(null, "server"));
                        if (TextUtils.isEmpty(camera.getService())) {
                            camera.setService(ToolUtil.DEVICE_SERVICE);
                        }
                        camera.setGroup(Integer.parseInt(parser.getAttributeValue(null, "Group")));
                        camera.setStorageUsr(parser.getAttributeValue(null, "storageusr"));
                        camera.setStoragePwd(parser.getAttributeValue(null, "storagepwd"));
                        camera.setStorageUrl(parser.getAttributeValue(null, "storageurl"));

                    }
                } else if (event == XmlPullParser.END_TAG) {
                    String name = parser.getName();
                    if ("Remote".equals(name)) {
                        // if (camera.getDevType() != ToolUtil.DEV_TYPE_DVR){
                        list.add(camera);
                        // }
                    }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * 用户添加设备
     */
    public String add_device(CameraRec camera) {
        String add_device_message = send_camera_message(camera);
        String add_device_report = reportToServer(add_device_message, LOGIN_URL);
        String add_device_response_result = analysisAddDeviceResponse(add_device_report);
        return add_device_response_result;
    }

    public String send_camera_message(CameraRec camera) {
        StringWriter writer = new StringWriter();

        XmlSerializer serializer = Xml.newSerializer();

        if (camera != null) {
            try {
                serializer.setOutput(writer);
                serializer.startDocument("gbk", true);
                serializer.startTag(null, "SPPacket");

                serializer.startTag(null, "MsgType");
                serializer.text(REQUEST_TYPE_ADD_DEVICE);
                serializer.endTag(null, "MsgType");

                serializer.startTag(null, "Version");
                serializer.text(LOGIN_VERSION);
                serializer.endTag(null, "Version");

                serializer.startTag(null, "ServerID");
                serializer.text("2");
                serializer.endTag(null, "ServerID");

                serializer.startTag(null, "UserID");
                serializer.text(ToolUtil.LOGIN_NAME);
                serializer.endTag(null, "UserID");

                serializer.startTag(null, "Remote");
                serializer.attribute(null, "ID", "0");
                if (camera.mac != null) {
                    serializer.attribute(null, "MacAdr", camera.mac);
                }
                serializer.attribute(null, "SN", camera.getSn());
                serializer.attribute(null, "ActiveID", camera.getSn());
                serializer.attribute(null, "ViewUser", camera.getUsername());
                serializer.attribute(null, "ViewPwd", camera.getPassword());
                serializer.attribute(null, "TcpPort", camera.getPort() + "");
                serializer.attribute(null, "UdpPort", camera.getUdpPort() + "");
                serializer.attribute(null, "IpcName", camera.getName());
                serializer.attribute(null, "ChannelNum", camera.getChanneNum() + "");
                serializer.attribute(null, "ChannelName", camera.getChanneName());
                serializer.attribute(null, "DevType", camera.getDevType() + "");
                serializer.attribute(null, "DevNum", camera.getDevNum() + "");
                serializer.endTag(null, "Remote");

                serializer.endTag(null, "SPPacket");
                serializer.endDocument();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toString();
        }
        return null;
    }

    private String analysisAddDeviceResponse(String response) {
        String result = 3 + "";
        if (TextUtils.isEmpty(response)) {
            return result;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(response));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (event == XmlPullParser.START_TAG && "Remote".equals(name)) {
                    String id = parser.getAttributeValue(null, "ID");
                    if ("0".equals(id)) {
                        String ret = parser.getAttributeValue(null, "Result");

                        Log.i("iii", "ret---------------" + ret);

                        return ret;
                    }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 设备管理中的删除设备
     */
    public String delete_device(CameraRec camera) {
        String delete_send_message = send_delete_message(camera);
        String delete_response = reportToServer(delete_send_message, LOGIN_URL);
        String delete_response_result = analysisLoginResponse(delete_response);
        return delete_response_result;
    }

    private String send_delete_message(CameraRec camera) {
        if (camera.getSn() != null) {
            StringWriter writer = new StringWriter();
            XmlSerializer serializer = Xml.newSerializer();
            try {
                serializer.setOutput(writer);
                serializer.startDocument("gbk", true);
                serializer.startTag(null, "SPPacket");

                serializer.startTag(null, "MsgType");
                serializer.text(REQUEST_TYPE_DELETE_DEVICE);
                serializer.endTag(null, "MsgType");

                serializer.startTag(null, "Version");
                serializer.text(LOGIN_VERSION);
                serializer.endTag(null, "Version");

                serializer.startTag(null, "UserID");
                serializer.text(ToolUtil.LOGIN_NAME);
                serializer.endTag(null, "UserID");

                serializer.startTag(null, "DAID");
                serializer.text(camera.getSn());
                serializer.endTag(null, "DAID");

                serializer.endTag(null, "SPPacket");
                serializer.endDocument();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return writer.toString();
        }
        return null;

    }

    /*
     * 设备信息里的存储设置
     */
    public String get_device_cloud(CameraRec camera, String device_response) {
        return analysis_device_cloudResponse(camera, device_response);
    }

    public String device_cloud_message() {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(REQUEST_TYPE_DEVICE_CLOUD);
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(QR_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserToken");
            serializer.text(ToolUtil.LOGIN_NAME);
            serializer.endTag(null, "UserToken");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();

    }

    private String analysis_device_cloudResponse(CameraRec camera, String reaponse_cloud) {

        try {
            if (camera != null) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(new StringReader(reaponse_cloud));
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    if (event == XmlPullParser.START_TAG) {
                        String name = parser.getName();
                        if ("Remote".equals(name)) {
                            String id = parser.getAttributeValue(null, "DAID");
                            if (id.equals(camera.getSn())) {
                                camera.setStorageserver(parser.getAttributeValue(null,
                                        "Storageserver"));
                                camera.setStoragetoken(parser.getAttributeValue(null,
                                        "Storagetoken"));
                                camera.setServerTypeID(parser.getAttributeValue(null,
                                        "ServerTypeID"));
                                camera.setEffectTime(parser.getAttributeValue(null, "EffectTime"));
                                camera.setLongs(parser.getAttributeValue(null, "Longs"));
                                camera.setIsOpen(parser.getAttributeValue(null, "IsOpen"));
                                String cloud_result = parser.getAttributeValue(null, "IsOpen");

                                if (cloud_result != null) {
                                    return cloud_result;
                                }
                            }
                        }
                    }
                    event = parser.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 设备信息里的存储设置中获得的信息内容
     */
    public String get_device_cloud_message() {
        String get_send_message = device_get_cloud_message();
        String device_get_response = reportToServer(get_send_message, LOGIN_URL);
        String device_get_response_result = analysis_device_message(device_get_response);
        return device_get_response_result;
    }

    private String device_get_cloud_message() {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text(REQUEST_DEVICE_CLOUD_MESSAGE);
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(QR_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserToken");
            serializer.text(ToolUtil.LOGIN_NAME);
            serializer.endTag(null, "UserToken");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();

    }

    private String analysis_device_message(String reaponse_message) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(reaponse_message));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if ("Services".equals(name)) {
                        String server_name = parser.getAttributeValue(null, "ServerName");
                        String server_details = parser.getAttributeValue(null, "ServerDetials");
                        String server_typeID = parser.getAttributeValue(null, "ServerTypeID");
                        String result = server_name + "&" + server_details + "&" + server_typeID;
                        return result;
                    }
                }
                event = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String open_device_cloud(CameraRec came, String ser_id) {
        String open_send_message = device_open_cloud_message(came, ser_id);
        String device_open_response = reportToServer(open_send_message, LOGIN_URL);
        String device_open_response_result = analysisLoginResponse(device_open_response);
        return device_open_response_result;
    }

    private String device_open_cloud_message(CameraRec camera, String server_id) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        if (!TextUtils.isEmpty(camera.getRemoteID()) && !TextUtils.isEmpty(server_id)) {
            try {
                serializer.setOutput(writer);
                serializer.startDocument("gbk", true);
                serializer.startTag(null, "SPPacket");

                serializer.startTag(null, "MsgType");
                serializer.text(REQUEST_DEVICE_CLOUD_OPEN);
                serializer.endTag(null, "MsgType");

                serializer.startTag(null, "Version");
                serializer.text(QR_VERSION);
                serializer.endTag(null, "Version");

                serializer.startTag(null, "UserToken");
                serializer.text(ToolUtil.LOGIN_NAME);
                serializer.endTag(null, "UserToken");

                serializer.startTag(null, "RemoteID");
                serializer.text(camera.getSn());
                serializer.endTag(null, "RemoteID");

                serializer.startTag(null, "ServerTypeID");
                serializer.text(server_id);
                serializer.endTag(null, "ServerTypeID");

                serializer.endTag(null, "SPPacket");
                serializer.endDocument();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();

    }

    public String message_whether_open(CameraRec came) {
        String open_message = message_open(came);
        String message_open_response = reportToServer(open_message, LOGIN_URL);
        String message_open_response_result = analysisLoginResponse(message_open_response);
        return message_open_response_result;
    }

    public String message_open(CameraRec camera) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        if (!TextUtils.isEmpty(camera.getSn()) && !TextUtils.isEmpty(ToolUtil.LOGIN_NAME)) {
            try {
                serializer.setOutput(writer);
                serializer.startDocument("gbk", true);
                serializer.startTag(null, "SPPacket");

                serializer.startTag(null, "MsgType");
                serializer.text(REQUEST_MESSAGE_NOTICE_OPEN);
                serializer.endTag(null, "MsgType");

                serializer.startTag(null, "Version");
                serializer.text(QR_VERSION);
                serializer.endTag(null, "Version");

                serializer.startTag(null, "UserOwer");
                serializer.text(ToolUtil.LOGIN_NAME);
                serializer.endTag(null, "UserOwer");

                serializer.startTag(null, "DAID");
                serializer.text(camera.getSn());
                serializer.endTag(null, "DAID");

                serializer.endTag(null, "SPPacket");
                serializer.endDocument();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    public String close_message_open(CameraRec came, String type) {
        String close_message = message_notice_close(came, type);
        String message_close_response = reportToServer(close_message, LOGIN_URL);
        String message_close_response_result = analysisLoginResponse(message_close_response);
        return message_close_response_result;
    }

    public String message_notice_close(CameraRec camera, String type) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        if (!TextUtils.isEmpty(camera.getSn()) && !TextUtils.isEmpty(type)) {
            try {
                serializer.setOutput(writer);
                serializer.startDocument("gbk", true);
                serializer.startTag(null, "SPPacket");

                serializer.startTag(null, "MsgType");
                serializer.text(REQUEST_MESSAGE_NOTICE_SET);
                serializer.endTag(null, "MsgType");

                serializer.startTag(null, "Version");
                serializer.text(QR_VERSION);
                serializer.endTag(null, "Version");

                serializer.startTag(null, "UserOwer");
                serializer.text(ToolUtil.LOGIN_NAME);
                serializer.endTag(null, "UserOwer");

                serializer.startTag(null, "DAID");
                serializer.text(camera.getSn());
                serializer.endTag(null, "DAID");

                serializer.startTag(null, "Type");
                serializer.text(type);
                serializer.endTag(null, "Type");

                serializer.endTag(null, "SPPacket");
                serializer.endDocument();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    public String findPassword(String username) {
        String find_message = findPasswordFromServer(username);
        String findPassword_response = reportToServer(find_message, LOGIN_URL);
        String findPassword_response_result = analysisQRResponse(findPassword_response);
        return findPassword_response_result;
    }

    /**
     * 连接服务器发送查找密码请求
     * 
     * @param username 登录用户名
     * @return
     */
    private static String findPasswordFromServer(String username) {
        if (username == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("gbk", true);
            serializer.startTag(null, "SPPacket");

            serializer.startTag(null, "MsgType");
            serializer.text("UseFindPwdReq");
            serializer.endTag(null, "MsgType");

            serializer.startTag(null, "Version");
            serializer.text(ToolUtil.REQUEST_VERSION);
            serializer.endTag(null, "Version");

            serializer.startTag(null, "UserEmail");
            serializer.text(username);
            serializer.endTag(null, "UserEmail");

            serializer.startTag(null, "SystemDomain");
            serializer.text("");
            serializer.endTag(null, "SystemDomain");

            serializer.endTag(null, "SPPacket");
            serializer.endDocument();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    // 获取onvif设备 add 2013.11.29
    public static List<onvCameraRec> getOnvifDevices() {
        List<onvCameraRec> deviceList = new ArrayList<onvCameraRec>();
        onvDevice[] onvdevice = JNI.onvifDevices();
        if (onvdevice == null) {
            return deviceList;
        }
        for (onvDevice device : onvdevice) {
            Log.i("i", "onvif device.ipaddr ==" + ipaddrSplit(device.ipaddr));
            onvCameraRec camera = new onvCameraRec();
            camera.ipaddr = ipaddrSplit(device.ipaddr);
            deviceList.add(camera);
        }

        return deviceList;
    }

    public static String ipaddrSplit(String ipaddr) {
        String[] httpip = new String[2];
        String[] iponvif = new String[2];
        httpip = ipaddr.split("//");
        iponvif = httpip[1].split("/|:");
        return iponvif[0];
    }

    public boolean getBaseInfo(CameraRec camera) {
        // CommonMethod common_method = CommonMethod.getInstance();
        if (camera == null)
            return false;
        if (camera.connty < 10000) {
            if (!ToolUtil.connectDevice(camera)) {
                return false;
            }
        }
        camera.baseinfo = JNI.GetBaseInfo(camera.connty);
        if (camera.baseinfo != null) {
            return true;
        } else {
            ToolUtil.destroyConnect(camera);
        }
        return false;
    }

}
