
package org.yeah.widget;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import org.yeah.util.Tools;

import java.io.IOException;

public class AudioUtil {
	private final static String TAG = "AudioUtil";

	public static final String AUDOI_DIR = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/audio";// 录音音频保存根路径

	private String mAudioPath;
	private boolean mIsPlaying;
	private boolean mIsRecording;
	private MediaPlayer mPlayer;
	private MediaRecorder mRecorder;

	// 获取音量值，只是针对录音音量
	public int getVolumn() {
		int volumn = 0;

		// 录音
		if (mRecorder != null && mIsRecording) {
			volumn = mRecorder.getMaxAmplitude();
			Tools.Log("volumn: " + volumn);
			if (volumn != 0)
				volumn = (int) (10 * Math.log(volumn) / Math.log(10)) / 7;
		}

		return volumn;
	}

	// 初始化 录音器
	private void initRecorder() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(mAudioPath);
		mIsRecording = true;
	}

	public boolean isPlaying() {
		return mIsPlaying;
	}

	// 开始录音，并保存到文件中
	public void recordAudio() {
		if (mAudioPath != null && !mAudioPath.equals("")) {
			initRecorder();
			try {
				mRecorder.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}

			mRecorder.start();
		}
		else {
			Log.d(TAG, "AudioUtil 启动录音失败！");
		}
	}

	// 设置保存路径
	public void setAudioPath(String path)
	{
		this.mAudioPath = path;
	}

	// 开始播放
	public void startPlay() {
		if (mAudioPath != null && !mAudioPath.equals("")) {
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(mAudioPath);
				mPlayer.prepare();
				mPlayer.start();
				mIsPlaying = true;
				mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
				{
					@Override
					public void onCompletion(MediaPlayer mp)
					{
						mp.release();
						mPlayer = null;
						mIsPlaying = false;
					}
				});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Log.d(TAG, "AudioUtil 播放录音音频失败！");
		}
	}

	// 停止录音
	public void stopRecord() {
		if (mRecorder != null)
		{
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
			mIsRecording = false;
		}
	}

}
