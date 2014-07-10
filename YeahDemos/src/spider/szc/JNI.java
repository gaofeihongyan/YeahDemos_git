package spider.szc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import android.R.integer;

interface EventCallback {
	public void alertTriggered(int alert, int on);
}
public class JNI {
	public static native int init(String server1, String server2);
	public static native void uninit();
	public static native Device[] enumDevices();
	//Return: < 1000: error code otherwise: handle of connection
	public static native int createConn(String service, String sn, String user, String password);
	public static native int NewcreateConn(String service, String sn, String user, String password, int conntype);
	public static native int connType(String service, String sn, String user, String password,int nettype);
	public static native int destroyConn(int hConn);
	public static native int getConnInfo(int hConn);
	
//	public static native int startLiveVideo(int hConn, int quality, int vstrm, ColorArray ca, int which);
	public static native int startLiveVideo(int hConn, int quality, int vstrm, int channel, ColorArray colorArray);
	
	public static native void stopLiveVideo(int hConn);
	public static native int startLiveAudio(int hConn);
	public static native void stopLiveAudio(int hConn);
	public static native void setEventCallback(EventCallback cb);
	public static native void ptzControl(int hConn, int action, int param1, int param2);
	//public static native void signalAlarmOut(int hConn, int oChn);
	public static native int startRecord(int hConn, String filePath, String camName);
	public static native void stopRecord(int hConn);
	
	public static native int[] listDate(int hConn);
	public static native Event[] listEvent(int hConn, String date);
	public static native void playEvent(long utc_time);
	/* Local Playback*/
	public static native int startPlayFile(int hConn/*0 for local file*/, String filePath, ColorArray ca);
	public static native void stopPlayFile(int handle);
	public static native void pausePlayFile(int handle);
	public static native void continuePlayFile(int handle);
	public static native void setPlaybackRate(int handle, int rate);
	public static native void seekPlayback(int handle, int milliseconds);
	// add 2013 2.2 
	public static native int getRotation(int handle, int vchn);	//<0: err; 0:normal, 1:h-flip; 2:v-flip; 3: turnover
	public static native int setRotation(int handle, int vchn, int rot/*0~3*/);
	public static native int getPowerFreq(int handle);	//<0: err; 0-50Hz; 1-60Hz
	public static native int setPowerFreq(int handle, int freq);	//freq: 0-50hz, 1-60hz
	public static native WifiAp[] listWifiAp(int handle);
	public static native WifiCfg getWifi(int handle);
	public static native int setWifi(int handle, WifiCfg cfg);
	public static native VideoColor getVideoColor(int handle, int vchn);
	public static native int setVideoColor(int handle, int vchn, VideoColor color);
	public static native VideoEncParam getVideoEncParam(int handle, int vchn, int vstream/*0~2*/);
	public static native int setVideoEncParam(int handle, int vchn, int vstream, VideoEncParam param);
	public static native int changePassword(int handle,String newPassword);
	public static native int sendAudio(int hConn, byte[] in_buf, byte[] out_buf, int in_len, int fmt);
	public static native int stopAudio(int hConn);
	
	final int WIFI_ENCTYPE_INVALID = 0;
	final int WIFI_ENCTYPE_NONE = 1;
	final int WIFI_ENCTYPE_WEP = 2;
	final int WIFI_ENCTYPE_WPA_TKIP = 3;
	final int WIFI_ENCTYPE_WPA_AES = 4;
	final int WIFI_ENCTYPE_WPA2_TKIP = 5;
	final int WIFI_ENCTYPE_WPA2_AES = 6;
	static public class WifiAp {
		public String essid;
		public int encType;		//WIFI_ENCTYPE_xxx
		public int quality;		//0~100
		
		public WifiAp() {
			quality = encType = 0;
		}
	}
	static public class WifiCfg {
		public String essid;
		public int encType;
		public String password;
		public WifiCfg() {
			encType = 0;
		}
	}
	
	static public class VideoColor {
		public int brightness;	//0~256, default 128
		public int contrast;
		public int saturation;
		public int hue;
		
		public VideoColor() {
			brightness = contrast = saturation = hue = 50;
		}
	}
	
	final int VRES_QQVGA = 0;//176x220
	final int VRES_QVGA	= 1;//320×240
	final int VRES_VGA= 3;//640×480
	final int VRES_720P = 5;//1280x720
	final int VRES_960P = 6;//1280×960
	
	static public class VideoEncParam {
		public int resolution;		//VRES_xxx
		public int fps;
		public int kbps;
		public int i_gops;
		
		public int supported_res;	//combination of (1<<VRES_xxx)
		
		public VideoEncParam() {
			resolution = fps = kbps = i_gops = supported_res = 0;
		}
	}
	
	/* Result of search */
	static public class Device {
		public String name;
		public String sn;
		public String ip;
		public int port;
	//	public int connty;
		
		public String netmask;
		public String mdns;
		public String sdns;
		public String ddnsuser;
		public String ddnsp;
		public String ddn;
		public String gw;
		public String mac;
		public String version;
		public int dhcpen;
		
		public String dChanneNum;
		public String dChanneName;
		public String dDomainname;
		public String dUdpPort;
		public String server;
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getNetmask() {
			return netmask;
		}
		public void setNetmask(String netmask) {
			this.netmask = netmask;
		}
		public String getMdns() {
			return mdns;
		}
		public void setMdns(String mdns) {
			this.mdns = mdns;
		}
		public String getSdns() {
			return sdns;
		}
		public void setSdns(String sdns) {
			this.sdns = sdns;
		}
		public String getDdnsuser() {
			return ddnsuser;
		}
		public void setDdnsuser(String ddnsuser) {
			this.ddnsuser = ddnsuser;
		}
		public String getDdnsp() {
			return ddnsp;
		}
		public void setDdnsp(String ddnsp) {
			this.ddnsp = ddnsp;
		}
		public String getDdn() {
			return ddn;
		}
		public void setDdn(String ddn) {
			this.ddn = ddn;
		}
		public String getGw() {
			return gw;
		}
		public void setGw(String gw) {
			this.gw = gw;
		}
		public String getMac() {
			return mac;
		}
		public void setMac(String mac) {
			this.mac = mac;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public int getDhcpen() {
			return dhcpen;
		}
		public void setDhcpen(int dhcpen) {
			this.dhcpen = dhcpen;
		}
		public String getdChanneNum() {
			return dChanneNum;
		}
		public void setdChanneNum(String dChanneNum) {
			this.dChanneNum = dChanneNum;
		}
		public String getdChanneName() {
			return dChanneName;
		}
		public void setdChanneName(String dChanneName) {
			this.dChanneName = dChanneName;
		}
		public String getdDomainname() {
			return dDomainname;
		}
		public void setdDomainname(String dDomainname) {
			this.dDomainname = dDomainname;
		}
		public String getdUdpPort() {
			return dUdpPort;
		}
		public void setdUdpPort(String dUdpPort) {
			this.dUdpPort = dUdpPort;
		}
		Device() { }
		Device(String _sn, String _ip) {
			sn = _sn;
			ip = _ip;
		}
		@Override
		public String toString() {
			return sn + "\n" + ip;
		}
	}
	
	static public class ConnInfo {
		public int ct;
	}
	
	static public class Event {
		public EventType et;
		long utc_time;
	}

	public enum EventType {
		EVENT_IO,
		EVENT_MD,
		EVENT_SND
	}
	public enum ConnType {
		CONNTYPE_LOCAL,
		CONNTYPE_P2P,
		CONNTYPE_RELAY
	}
	public enum PTZACTION {
		PTZ_STOP, 
		PTZ_MOVE_UP, 
		PTZ_MOVE_UP_STOP,
		PTZ_MOVE_DOWN,
		PTZ_MOVE_DOWN_STOP,
		PTZ_MOVE_LEFT,
		PTZ_MOVE_LEFT_STOP,
		PTZ_MOVE_RIGHT, 
		PTZ_MOVE_RIGHT_STOP,
		PTZ_MOVE_UPLEFT, 
		PTZ_MOVE_UPLEFT_STOP,
		PTZ_MOVE_DOWNLEFT, 
		PTZ_MOVE_DOWNLEFT_STOP,
		PTZ_MOVE_UPRIGHT, 
		PTZ_MOVE_UPRIGHT_STOP,
		PTZ_MOVE_DOWNRIGHT, 
		PTZ_MOVE_DOWNRIGHT_STOP,
		PTZ_IRIS_IN, 
		PTZ_IRIS_IN_STOP, 
		PTZ_IRIS_OUT, 
		PTZ_IRIS_OUT_STOP, 
		PTZ_FOCUS_ON, 
		PTZ_FOCUS_ON_STOP, 
		PTZ_FOCUS_OUT, 
		PTZ_FOCUS_OUT_STOP, 
		PTZ_ZOOM_IN, 
		PTZ_ZOOM_IN_STOP, 
		PTZ_ZOOM_OUT, 
		PTZ_ZOOM_OUT_STOP, 

		PTZ_SET_PSP, 
		PTZ_CALL_PSP, 
		PTZ_DELETE_PSP, 

		PTZ_BEGIN_CRUISE_SET, 
		PTZ_SET_CRUISE, 
		PTZ_END_CRUISE_SET, 
		PTZ_CALL_CRUISE, 
		PTZ_DELETE_CRUISE, 
		PTZ_STOP_CRUISE, 

		/* DVS 内置协议没有的命令 */

		PTZ_AUTO_SCAN, 
		PTZ_AUTO_SCAN_STOP,

		PTZ_RAINBRUSH_START, 
		PTZ_RAINBRUSH_STOP,
		PTZ_LIGHT_ON, 
		PTZ_LIGHT_OFF,

		PTZ_MAX 
	};
	
	//20130719 
	public static native VideoParam[] GetVideoParamNew(int conn);
	public static native int SetVideoParamNew(int conn, VideoParam param);
	public static native int DefaultConn(int conn, int param);
	
	public static class VideoParam{
		public int res;
		public int quality;
		public int fps;
		public int brmode;
		public int save;
		public int kbps;
		public int gop;
		public int max_fps;
		@Override
		public String toString() {
			return "VideoParam [res=" + res + ", quality=" + quality + ", fps="
					+ fps + ", brmode=" + brmode + ", save=" + save + ", kbps="
					+ kbps + ", gop=" + gop + ", max_fps=" + max_fps + "]";
		}
	}
	
	public static class BaseInfo implements Serializable{
		public String daID;
		public String versionApi;
		public String mainuser;
		public String sSn;
		public String mac;
		public String name;
		public String version;
		public int type;
		public int chnum;
		public int AllSize;
		public int FreeSize;

		@Override
		public String toString() {
			return "BaseInfo [daID=" + daID + ", versionApi=" + versionApi
					+ ", mainuser=" + mainuser + ", sSn=" + sSn + ", mac="
					+ mac + ", name=" + name + ", version=" + version
					+ ", type=" + type + ", chnum=" + chnum + ", AllSize="
					+ AllSize + ", FreeSize=" + FreeSize + "]";
		}
	}
	
	public static native int getNetInfo(int conn, NetInfo netInfo);
	public static native int setNetInfo(int conn, NetInfo netInfo);
	
	public static native int getMailInfo(int conn, MailInfo mailInfo);
	public static native int setMailInfo(int conn, MailInfo mailInfo);
	
	public static native int getTimeInfo(int conn, TimeInfo timeInfo);
	public static native int setTimeInfo(int conn, TimeInfo timeInfo);
	
	public static native BaseInfo GetBaseInfo(int conn);
	
	public static native int setInitInfo(int conn, int initInfo);	// //0:reboot 1:Restore factory settings
	
	public static class NetInfo{
		public int ipType;		//0:Fixed IP  1:dynamic ip
		public int dnsType;		//0:no		  1:yes
		public String ip;
		public String netmask;
		public String gateway;
		public String mainDns;
		public String secondDns;
		public int port;
		
		public int getIpType() {
			return ipType;
		}
		public void setIpType(int ipType) {
			this.ipType = ipType;
		}
		public int getDnsType() {
			return dnsType;
		}
		public void setDnsType(int dnsType) {
			this.dnsType = dnsType;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getNetmask() {
			return netmask;
		}
		public void setNetmask(String netmask) {
			this.netmask = netmask;
		}
		public String getGateway() {
			return gateway;
		}
		public void setGateway(String gateway) {
			this.gateway = gateway;
		}
		public String getMainDns() {
			return mainDns;
		}
		public void setMainDns(String mainDns) {
			this.mainDns = mainDns;
		}
		public String getSecondDns() {
			return secondDns;
		}
		public void setSecondDns(String secondDns) {
			this.secondDns = secondDns;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
	}

	public static class MailInfo{
		public String receiverAddr;
		public String serverAddr;
		public String username;
		public String password;
		public String sendAddr;
		public int port;
		public int authenticate;		//0:no  1:need
		public int encryptionType;		//0:none 1:ssl	2:tls
		
		public String getReceiverAddr() {
			return receiverAddr;
		}
		public void setReceiverAddr(String receiverAddr) {
			this.receiverAddr = receiverAddr;
		}
		public String getServerAddr() {
			return serverAddr;
		}
		public void setServerAddr(String serverAddr) {
			this.serverAddr = serverAddr;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getSendAddr() {
			return sendAddr;
		}
		public void setSendAddr(String sendAddr) {
			this.sendAddr = sendAddr;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public int getAuthenticate() {
			return authenticate;
		}
		public void setAuthenticate(int authenticate) {
			this.authenticate = authenticate;
		}
		public int getEncryptionType() {
			return encryptionType;
		}
		public void setEncryptionType(int encryptionType) {
			this.encryptionType = encryptionType;
		}
	}
	
	public static class TimeInfo{
		public DateTime nowTime;
		public int timeMode;		// 0:NONETIME, 1:SaveCurrent, 2:ManuallySet, 3:SynchronizationComputer, 4:SynchronizationNTP
		public int timeArea; 		// TimeArea enum
		public DateTime setTime;
		public String timeServer;
		public int interval;		// hour
		public String gmtInfo;		// 例如GMT+08:00
		
		public DateTime getNowTime() {
			return nowTime;
		}
		public void setNowTime(DateTime nowTime) {
			this.nowTime = nowTime;
		}
		public int getTimeMode() {
			return timeMode;
		}
		public void setTimeMode(int timeMode) {
			this.timeMode = timeMode;
		}
		public int getTimeArea() {
			return timeArea;
		}
		public void setTimeArea(int timeArea) {
			this.timeArea = timeArea;
		}
		public DateTime getSetTime() {
			return setTime;
		}
		public void setSetTime(DateTime setTime) {
			this.setTime = setTime;
		}
		public String getTimeServer() {
			return timeServer;
		}
		public void setTimeServer(String timeServer) {
			this.timeServer = timeServer;
		}
		public int getInterval() {
			return interval;
		}
		public void setInterval(int interval) {
			this.interval = interval;
		}
		public String getGmtInfo(){
			return gmtInfo;
		}
		public void setGmtInfo(String gmt){
			this.gmtInfo = gmt;
		}
	}
	
	public static class DateTime{
		public int year;
		public int mon;
		public int day;
		public int hour;
		public int min;
		public int sec;
		
		public DateTime(){
			Calendar cal = Calendar.getInstance();  
	        cal.setTimeInMillis(System.currentTimeMillis());
	        setYear(cal.get(Calendar.YEAR));
	        setMon(cal.get(Calendar.MONTH)+1);
	        setDay(cal.get(Calendar.DAY_OF_MONTH));
	        setHour(cal.get(Calendar.HOUR_OF_DAY));
	        setMin(cal.get(Calendar.MINUTE));
	        setSec(cal.get(Calendar.SECOND));
		}
		
		public DateTime(int year, int mon, int day, int hour, int min, int sec){
			this.year = year;
			this.mon = mon;
			this.day = day;
			this.hour = hour;
			this.min = min;
			this.sec = sec;
		}
		
		public static DateTime getDateTime(long timeMills){
			DateTime time = new DateTime();
			Calendar cal = Calendar.getInstance();  
	        cal.setTimeInMillis(System.currentTimeMillis());
	        time.setYear(cal.get(Calendar.YEAR));
	        time.setMon(cal.get(Calendar.MONTH)+1);
	        time.setDay(cal.get(Calendar.DAY_OF_MONTH));
	        time.setHour(cal.get(Calendar.HOUR_OF_DAY));
	        time.setMin(cal.get(Calendar.MINUTE));
	        time.setSec(cal.get(Calendar.SECOND));
	        return time;
		}
		public String toHourMinuteTime(){
			return String.format("%1$02d:%2$02d", hour, min);
		}
		
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getMon() {
			return mon;
		}
		public void setMon(int mon) {
			this.mon = mon;
		}
		public int getDay() {
			return day;
		}
		public void setDay(int day) {
			this.day = day;
		}
		public int getHour() {
			return hour;
		}
		public void setHour(int hour) {
			this.hour = hour;
		}
		public int getMin() {
			return min;
		}
		public void setMin(int min) {
			this.min = min;
		}
		public int getSec() {
			return sec;
		}
		public void setSec(int sec) {
			this.sec = sec;
		}
	}
	
	public static native int getMotionInfo(int conn, MotionInfo motionInfo);
	public static native int setMotionInfo(int conn, MotionInfo motionInfo);
	public static native int getSoundInfo(int conn, SoundInfo soundInfo);
	public static native int setSoundInfo(int conn, SoundInfo soundInfo);
	public static native int getIOInfo(int conn, IOInfo ioInfo);
	public static native int setIOInfo(int conn, IOInfo ioInfo);
	public static native int getCoverInfo(int conn, CoverInfo coverInfo);
	public static native int setCoverInfo(int conn, CoverInfo coverInfo);
	public static native int getCaptureInfo(int conn , CaptureInfo captureInfo);
	public static native int setCaptureInfo(int conn , CaptureInfo captureInfo);
	public static native int getVideoInfo(int conn, VideoInfo videoInfo);
	public static native int setVideoInfo(int conn, VideoInfo videoInfo);
	public static native int getScene(int conn);
	public static native int setScene(int conn, int scene);
	
	public static class MotionInfo{
		public int channel;		// ipc=0
		public int open;		// 0:close  1:open
		public int email;		// 0:no  1:yes   Mail notification
		public int output;		// 0:no  1:yes   Trigger alarm output
		
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpen() {
			return open == 1;
		}
		public void setOpen(boolean open) {
			this.open = open? 1:0;
		}
		public boolean getEmail() {
			return email == 1;
		}
		public void setEmail(boolean email) {
			this.email = email? 1:0;
		}
		public boolean getOutput() {
			return output == 1;
		}
		public void setOutput(boolean output) {
			this.output = output?1:0;
		}
		@Override
		public String toString() {
			return "MotionInfo [channel=" + channel + ", open=" + open
					+ ", email=" + email + ", output=" + output + "]";
		}
		
		
	}
	
	public static class SoundInfo{
		public int channel;		//ipc=0
		public int open;		//0:close  1:open
		public int email;		//0:no  1:yes   Mail notification
		public int output;		//0:no  1:yes   Trigger alarm output
		public int sensitivity;//0~6	0:NONESensitivity 1:lowerest 2:lower 3:General 4:High 5:Highest 6:Auto
		
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpen() {
			return open == 1;
		}
		public void setOpen(boolean open) {
			this.open = open?1:0;
		}
		public boolean getEmail() {
			return email == 1;
		}
		public void setEmail(boolean email) {
			this.email = email?1:0;
		}
		public boolean getOutput() {
			return output == 1;
		}
		public void setOutput(boolean output) {
			this.output = output? 1:0;
		}
		public int getSensitivity() {
			return sensitivity;
		}
		public void setSensitivity(int sensitivity) {
			this.sensitivity = sensitivity;
		}
	}
	
	public static class IOInfo{
		public int channel;		//ipc=0
		
		public int openInput;	//0:close  1:open
		public int level;		//0:lower  1:high
		
		public int openOutput;	//0:no  1:yes   Trigger alarm output
		public int putType;		//0:always close   1:always open
		public int putTime;		//s
		
		public int linkEmail;	//0:no  1:yes   Mail notification
		public int linkPic;		//0:no  1:yes  Whether with pictures
		public int linkAlarm;	//0:no  1:yes   Trigger alarm output
		public int linkPTZ;		//0~15
		
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpenInput() {
			return openInput == 1;
		}
		public void setOpenInput(boolean openInput) {
			this.openInput = openInput? 1:0;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public boolean getOpenOutput() {
			return openOutput == 1;
		}
		public void setOpenOutput(boolean openOutput) {
			this.openOutput = openOutput?1:0;
		}
		public int getPutType() {
			return putType;
		}
		public void setPutType(int putType) {
			this.putType = putType;
		}
		public int getPutTime() {
			return putTime;
		}
		public void setPutTime(int putTime) {
			this.putTime = putTime;
		}
		public boolean getLinkEmail() {
			return linkEmail == 1;
		}
		public void setLinkEmail(boolean linkEmail) {
			this.linkEmail = linkEmail?1:0;
		}
		public boolean getLinkPic() {
			return linkPic == 1;
		}
		public void setLinkPic(boolean linkPic) {
			this.linkPic = linkPic?1:0;
		}
		public boolean getLinkAlarm() {
			return linkAlarm == 1;
		}
		public void setLinkAlarm(boolean linkAlarm) {
			this.linkAlarm = linkAlarm?1:0;
		}
		public int getLinkPTZ() {
			return linkPTZ;
		}
		public void setLinkPTZ(int linkPTZ) {
			this.linkPTZ = linkPTZ;
		}
	}
	
	public static class CoverInfo{
		public int channel;			//ipc=0
		public int open;			//0:close  1:open
		public int sensitivity;		//0~6	0:NONESensitivity 1:lowerest 2:lower 3:General 4:High 5:Highest 6:Auto
		
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpen() {
			return open == 1;
		}
		public void setOpen(boolean open) {
			this.open = open? 1:0;
		}
		public int getSensitivity() {
			return sensitivity;
		}
		public void setSensitivity(int sensitivity) {
			this.sensitivity = sensitivity;
		}
	}
	
	public static class CaptureInfo{
		public int channel;			//ipc=0
		public int open;			//0:close  1:open
		public int interval;		//min
		public int email;			//0:no  1:yes   Mail notification
		
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpen() {
			return open == 1;
		}
		public void setOpen(boolean open) {
			this.open = open?1:0;
		}
		public int getInterval() {
			return interval;
		}
		public void setInterval(int interval) {
			this.interval = interval;
		}
		public boolean getEmail() {
			return email == 1;
		}
		public void setEmail(boolean email) {
			this.email = email?1:0;
		}
	}
	
	public static class VideoInfo{
		public int channel;			//ipc=0
		public int open;			//0:close  1:open
		public int packTime;		//min   Packing time
		public int getChannel() {
			return channel;
		}
		public void setChannel(int channel) {
			this.channel = channel;
		}
		public boolean getOpen() {
			return open == 1;
		}
		public void setOpen(boolean open) {
			this.open = open?1:0;
		}
		public int getPackTime() {
			return packTime;
		}
		public void setPackTime(int packTime) {
			this.packTime = packTime;
		}
	}
	
	public static native int getOsdInfo(int conn, OsdInfo osdInfo);
	public static native int setOsdInfo(int conn, OsdInfo osdInfo);

	public static class OsdInfo{
		public int needData;		// 0:yes 1:no
		public int needDevName;		// 0:yes 1:no
		public int dataPos;		// 0:leftup 1:leftdown 2:rightup 3:rightdown
		public int devNamePos;
		public String otherInfo;
		public int otherPos;
	}
	
	
	// 获取远程缩略图（保存到savePath路径）
	public static native int getSnapshot(int hConn, String savePath);
	
	// 云报警信息
	public static native int getCloudAlarmInfo(int conn, CloudAlarmInfo cloudAlarmInfo);
	public static native int setCloudAlarmInfo(int conn, CloudAlarmInfo cloudAlarmInfo);

	public static class CloudAlarmInfo{
		public int isOpenMotion;	// 移动侦测 0：关闭  1：开启
		public int motionLevel;		// 灵敏度： 0：低    1：一般  2：灵敏

		public int isOpenSound;		// 声音侦测 0：关闭  1：开启
		public int soundLevel;		// 灵敏度： 0：低    1：一般  2：灵敏

		public int isOpenIO;		// I/O告警  0：关闭  1：开启
		public int ioLevel;		// 灵敏度： 0：低    1：一般  2：灵敏

		public int isOpenOther;		// 其他类型告警  0：关闭  1：开启
		public int otherLevel;		// 灵敏度： 0：低   1：一般  2：灵敏
	}
	
	//onvif devices 2013.11.28
	static public class onvDevice{
		public String ipaddr;

		@Override
		public String toString() {
			return ipaddr;
		}
	}
	
	public static native onvDevice[] onvifDevices();	//add 2013.11.29
	
	
	////////////////////////////SD' Reocrd///////////////////////////
	/**
	* 获取SD卡录像列表
	* @param connect
	* @param request
	* @param list
	* @return
	*/
	public static native int sdcardReocrdGetReocrdList(int connect, SearchRecordRequest request, SearchRecordResponse response);
	
	/**
	* 播放SD卡录像
	* @param connect
	* @param remoteRecord
	* @return
	*/
	public static native int sdcardReocrdPlaybackReocrd(int connect, SDCardRecord record, ColorArray colorArray);
	
	/**
	* 设置SD卡录像播放点
	* @param connect
	* @param position
	* @return
	*/
	public static native int sdcardRecordPlayBackSelectTime(int connect, SDCardRecord record);
	
	/**
	* 设置播放速率
	* @param connect
	* @param rate
	* @return
	*/
	public static native int sdcardRecordSetPlayRate(int connect, int rate);
	
	/**
	* 操纵播放动作
	* @param connect
	* @param action
	* @return
	*/
	public static native int sdcardRecordPauseOrRePlayVideo(int connect, int action);
	
	/**
	* 结束播放
	* @param connect
	* @param remoteRecord
	* @return
	*/
	public static native int sdcardRecordTerminatePlayBack(int connect, SDCardRecord remote);
	
	/**
	* 获取SD卡录像计划
	* @param connect
	* @param list
	* @return
	*/
	public static native int sdcardRecordGetRecordPlan(int connect, int channel, List<SDCardRecordPlan> list);
	
	/**
	* 设置SD卡录像计划
	* @param connect
	* @param plan
	* @return
	* 成功都是返回0，设备不支持返回17，其他都是失败（例如：-1内容填写错误，105连接超时）
	*/
	public static native int sdcardRecordSetRecordPlan(int connect, List<SDCardRecordPlan> list);
	
	/**
	* 格式化SD卡
	* @param connect
	* @return
	*/
	public static native int sdcardRecordFormatSDCard(int connect);
	
	public static int RECORD_TYPE_ALL = 0;			// 录像类型：所有
	public static int RECORD_TYPE_NORMAL = 1;		// 录像类型：存储事件
	public static int RECORD_TYPE_EVENT = 2;		// 录像类型：告警事件
	public static int RECORD_TYPE_MD = 3;			// 录像类型：移动侦测
	public static int RECORD_TYPE_INPUT = 4;		// 录像类型：输入输出
	public static int RECORD_TYPE_SOUND = 5;		// 录像类型：声音告警
	public static int RECORD_TYPE_OTHER = 6;		// 录像类型：其他告警
	
	public static int RECORD_TYPE_REQUEST_LOADMORE = 0;	// 请求：加载更多
	public static int RECORD_TYPE_REQUEST_REFRESH = 1;		// 请求：重新加载
	
	public static int PLAYBACK_RATE_HALFX = 0;		// 播放速率：0.5倍
	public static int PLAYBACK_RATE_1X = 1;		// 播放速率：1倍（正常）
	public static int PLAYBACK_RATE_2X = 2;		// 播放速率：2倍
	public static int PLAYBACK_RATE_4X = 3;		// 播放速率：4倍
	
	public static int PLAYBACK_ACTION_PAUSE = 0;	// 操纵动作：暂停
	public static int PLAYBACK_ACTION_REPLAY = 1;	// 操纵动作：重放
	
//	public static int PLAN_ACTION_ADD = 0;			// 计划操作：添加(不使用)
//	public static int PLAN_ACTION_MODIFY = 1;		// 计划操作：修改(不使用)
//	public static int PLAN_ACTION_DELETE = 2;		// 计划操作：删除(不使用)
	public static int PLAN_ACTION_OPEN = 3;		// 计划操作：打开
	public static int PLAN_ACTION_CLOSE = 4;		// 计划操作：关闭
	
	public static int WEEK_MONDAY = 1;				// 星期一
	public static int WEEK_TUESDAY = 2;			// 星期二
	public static int WEEK_WEDNESDAY = 4;			// 星期三
	public static int WEEK_THURASDAY = 8;			// 星期四
	public static int WEEK_FRIDAY = 16;			// 星期五
	public static int WEEK_SATURADY = 32;			// 星期六
	public static int WEEK_SUNDAY = 64;			// 星期日
		
	/**
	* 搜索SD卡录像请求
	*/
	public static class SearchRecordRequest{
		public int pageNum;			// 每页条数(默认50)
		public int currentPage;		// 当前页数
		public int flag;			// 0:加载更多  1：重新加载
		public int channel;			// 通道
		public int recordType;		// 存储类型     1 存储   2 告警
		public DateTime from;		// 开始时间
		public DateTime to;			// 结束时间
	}
		
	/**
	* 搜索SD卡录像响应
	*/
	public static class SearchRecordResponse{
		public int totalNum;
		public int firstNum;
		public List<SDCardRecord> list;
	}
		
	/**
	* SD卡录像
	*/
	public static class SDCardRecord{
		public int channel;			// 通道
		public int recordType;		// 计划类型
		public DateTime from;		// 开始时间
		public int timeLen;			// 持续时长（秒）
		
		public SDCardRecord clone(){
			SDCardRecord record = new SDCardRecord();
			record.channel = channel;
			record.recordType = recordType;
			record.from = new DateTime(from.year, from.mon, from.day, from.hour, from.min, from.sec);
			record.timeLen = timeLen;
			return record;
		}
	}
		
	/**
	*	SD卡录像计划
	*/
	public static class SDCardRecordPlan{
		public int channel;			// 通道
		public int planAction;		// 计划动作
		public DateTime from;		// 开始时间
		public DateTime to;			// 结束时间
		public int weekInfo;		// 星期
	
		/**
		* 获取星期信息
		* @return
		*/
		public boolean[] getWeekInfoByBoolean(){
			boolean[] info = new boolean[7];
			for (int i=0; i<7; i++){
				int tmp = 1<<i;
				info[i] = (tmp&weekInfo) == tmp;
			}
			return info;
		}
			
		/**
		* 设置星期信息
		* @param week
		*/
		public void setWeekInfoByBoolean(boolean[] week){
			weekInfo = 0;
			for (int i=0; i<week.length; i++){
				if (week[i]){
					weekInfo = (weekInfo | (1<<i));
				}
			}
		}
			
		@Override
		public SDCardRecordPlan clone(){
			SDCardRecordPlan plan = new SDCardRecordPlan();
			plan.channel = channel;
			plan.planAction = planAction;
			plan.weekInfo = weekInfo;
			plan.from = new DateTime(from.year, from.mon, from.day, from.hour, from.min, from.sec);
			plan.to = new DateTime(to.year, to.mon, to.day, to.hour, to.min, to.sec);
			return plan;
		}
	}
	///////////////////////////////////////////////////////////////////////////
}
