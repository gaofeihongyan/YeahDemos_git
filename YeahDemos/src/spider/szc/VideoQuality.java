
package spider.szc;

import java.util.HashMap;
import java.util.Map;

public class VideoQuality {

    private final static String TAG = VideoQuality.class.getSimpleName();

    private static Map<String, Integer> videoQuality;

    public final static int QUALITY_LOWEST = 0;
    public final static int QUALITY_LOW = 1;
    public final static int QUALITY_NORMAL = 2;
    public final static int QUALITY_HIGH = 3;
    public final static int QUALITY_HIGHEST = 4;
    public final static int QUALITY_AUTO = 101; // 100
    public final static int QUALITY_MANUALLY = 101;

    public static void initVideoQuality() {
        videoQuality = new HashMap<String, Integer>();
    }

    public static void uninitVideoQuality() {
        videoQuality = null;
    }

    public static void setVideoQuality(CameraRec camera, int quality) {
        if (videoQuality == null)
            return;

        String sn = camera.getSn();
        videoQuality.put(sn, quality);
    }

    public static int getVideoQuality(CameraRec camera) {
        if (videoQuality == null)
            return QUALITY_AUTO;
        String sn = camera.getSn();
        Integer quality = videoQuality.get(sn);
        if (quality == null) {
            quality = QUALITY_AUTO;
        }
        return quality;
    }

}
