package spider.szc;

public interface ColorArray {
    public boolean dimensionChanged(int width, int height); /*be called when the first frame arrived, or the resolution changed */
    public int[] getColorBuffer();  /* be called to create a color array to be filled by decoder */
    public void frameReady();       /* frame is prepared in the color array */
    public byte[] allocateAudioBuffer();    //allocate a buffer of 4K bytes��һ֡����ʱ������ռ�
    public void audioReady(int len);        //ÿ֡���󶼻����
    public void onProgress(int thousandth, int milliseconds); /* called by playback procedure ��ǰ���Ž���   ��һ������  �ڶ����Ǻ���*/
}
