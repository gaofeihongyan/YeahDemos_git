package spider.szc;

public interface ColorArray {
    public boolean dimensionChanged(int width, int height); /*be called when the first frame arrived, or the resolution changed */
    public int[] getColorBuffer();  /* be called to create a color array to be filled by decoder */
    public void frameReady();       /* frame is prepared in the color array */
    public byte[] allocateAudioBuffer();    //allocate a buffer of 4K bytes第一帧来的时候申请空间
    public void audioReady(int len);        //每帧来后都会调用
    public void onProgress(int thousandth, int milliseconds); /* called by playback procedure 当前播放进度   第一个是秒  第二个是毫秒*/
}
