
package spider.szc;

import spider.szc.JNI.BaseInfo;
import spider.szc.ToolUtil;

import java.io.Serializable;
import java.net.URLDecoder;

public class CameraRec implements Serializable {
    public String id;
    public String host;
    public int port;
    public String name;
    public String username;
    public String password;
    public int istream;
    public String sn;
    public String service;
    public String mac;
    public String netMask;
    public int connty;// add 2013.08.20
    public String mainDns;
    public String seconDns;
    public String ddnsuser;
    public String ddnsp;
    public String ddn;
    public String gw;
    public String version;
    public int dhcp;
    public int key;// 本地与平台的区别值 ；1为平台 ，2为本地,3为本地+平台
    public int channeNum;
    public String domainName;
    public String channeName;
    public String online;
    public int udpPort;
    public String remoteID;// 平台特有的
    public int isLoad;// 判断设备加载与否
    public int onlineType; // 0:离线 1:平台 2:本地 3:本地+平台 add 2013.10.18
    public int connType; // 0:Local 1:P2P 2:Relay
    public int devType;
    private int devNum;
    public BaseInfo baseinfo;
    public String device_cloud_space;

    public int group;
    public String storageUsr;
    public String storagePwd;
    public String storageUrl;

    /*
     * add 2013 11 29
     */
    public String Storageserver; // 云存储服务器地址
    public String Storagetoken;
    public String ServerTypeID; // 服务类型
    public String EffectTime; // 生效时间
    public String Longs; // 有效时长（月）
    public String IsOpen; // 开通 未开通
    public String DoType;

    private int videoStream = ToolUtil.VIDEO_MAX_STREAM; // 1~3
    private int videoQuality = ToolUtil.VIDEO_QUALITY_AUTO; // 0~4 101

    // public int dhcp;
    public int getIsLoad() {
        return isLoad;
    }

    public void setIsLoad(int isLoad) {
        this.isLoad = isLoad;
    }

    public String getRemoteID() {
        return remoteID;
    }

    public void setRemoteID(String remoteID) {
        this.remoteID = remoteID;
    }

    public String getId() {
        return id;
    }

    public void setId(String string) {
        this.id = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConnType() {
        return connType;
    }

    public void setConnType(int connType) {
        this.connType = connType;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNetMask() {
        return netMask;
    }

    public void setNetMask(String netMask) {
        this.netMask = netMask;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMdns() {
        return mainDns;
    }

    public void setMdns(String mdns) {
        this.mainDns = mdns;
    }

    public String getSdns() {
        return seconDns;
    }

    public void setSdns(String sdns) {
        this.seconDns = sdns;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getDhcpen() {
        return dhcp;
    }

    public void setDhcpen(int dhcpen) {
        this.dhcp = dhcpen;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getChanneNum() {
        return channeNum;
    }

    public void setChanneNum(int channeNum) {
        this.channeNum = channeNum;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getChanneName() {
        return channeName;
    }

    public void setChanneName(String channeName) {
        this.channeName = channeName;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getIstream() {
        return istream;
    }

    public void setIstream(int istream) {
        this.istream = istream;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(int onlineType) {
        this.onlineType = onlineType;
    }

    public int getDevNum() {
        return devNum;
    }

    public void setDevNum(int devNum) {
        this.devNum = devNum;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getStorageUsr() {
        return storageUsr;
    }

    public void setStorageUsr(String storageUsr) {
        this.storageUsr = storageUsr;
    }

    public String getStoragePwd() {
        return storagePwd;
    }

    public void setStoragePwd(String storagePwd) {
        this.storagePwd = storagePwd;
    }

    public String getStorageUrl() {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl) {
        this.storageUrl = storageUrl;
    }

    public int getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(int videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getStorageserver() {
        return Storageserver;
    }

    public void setStorageserver(String storageserver) {
        Storageserver = storageserver;
    }

    public String getStoragetoken() {
        return Storagetoken;
    }

    public void setStoragetoken(String storagetoken) {
        if (storagetoken.contains(":")) {
            Storagetoken = storagetoken;
        } else {
            Storagetoken = URLDecoder.decode(storagetoken);
        }
    }

    public String getServerTypeID() {
        return ServerTypeID;
    }

    public void setServerTypeID(String serverTypeID) {
        ServerTypeID = serverTypeID;
    }

    public String getEffectTime() {
        return EffectTime;
    }

    public void setEffectTime(String effectTime) {
        EffectTime = effectTime;
    }

    public String getLongs() {
        return Longs;
    }

    public void setLongs(String longs) {
        Longs = longs;
    }

    public String getIsOpen() {
        return IsOpen;
    }

    public void setIsOpen(String isOpen) {
        IsOpen = isOpen;
    }

    public String getDoType() {
        return DoType;
    }

    public void setDoType(String doType) {
        DoType = doType;
    }

    public CameraRec() {
        istream = 0;
    }

    public CameraRec(String _host, int _port, String _name, String _username, String _password,
            int _istream) {
        host = _host;
        port = _port;
        name = _name;
        username = _username;
        password = _password;
        istream = _istream;
    }

    public CameraRec(String _sn, String _name, String _username, String _password) {
        sn = _sn;
        name = _name;
        username = _username;
        password = _password;
    }

    @Override
    public String toString() {
        return "name:" + name + " host:" + host
                + " port:" + port + " sn:" + sn + " key:" + key
                + " channelName:" + channeName + " channelNum:"
                + ", connty=" + connty + channeNum + " udpPort:" + udpPort
                + " mac:" + mac + " netmask:" + netMask + " mdns:" + mainDns
                + " sdns:" + seconDns + " ddnsuser:" + ddnsuser
                + " ddnsp:" + ddnsp + " ddn:" + ddn + " gw:" + gw
                + " version:" + version + " istream:" + istream + " "
                + "connectType=" + onlineType
                + ", dhcp=" + dhcp + ", devType=" + devType + ", service=" + service
                + ", group=" + group + ", storageUsr=" + storageUsr + ", storagePwd=" + storagePwd
                + ", storageUrl=" + storageUrl;
    }

    static final long serialVersionUID = 1;
}
