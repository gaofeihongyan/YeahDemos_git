
package org.yeah.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.yeah.R;
import org.yeah.util.Tools;

import java.io.File;

public class RecordButton extends Button {

	private static final int MIN_INTERVAL_TIME = 2000;// 2s
	private final static String TAG = "RecordButton";
	public final static int VOLUME_MAX = 8;

	private AudioUtil mAudioUtil;
	private String mFileName = AudioUtil.AUDOI_DIR;
	private OnFinishedRecordListener mOnFinishedListerer;
	private OnStartRecordListener mOnStartListerer;

	private boolean mIsCancel = false;
	private Dialog mRecordDialog;
	private long mStartTime;
	private ObtainDecibelThread mThread;

	private Handler mVolumeHandler;

	private ImageView mVolumeIcon;
	private VolumeViewer mVolumeViewer;
	private TextView mText;
	private int mXpositon = 0;
	private int mYpositon = 0;
	private RectF hotArea;

	private OnDismissListener onDismiss;
	private OnShowListener onShow;

	public interface OnFinishedRecordListener {
		public void onFinishedRecord(boolean isCancel);
	}

	public interface OnStartRecordListener {
		public void onStartRecord();
	}

	public void setOnFinishedRecordListener(OnFinishedRecordListener listener) {
		mOnFinishedListerer = listener;
	}

	public void setOnStartRecordListener(OnStartRecordListener listener) {
		mOnStartListerer = listener;
	}

	public RecordButton(Context context) {
		super(context);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 结束录音
	private void finishRecord() {
		stopRecording();
		mRecordDialog.dismiss();

		// long intervalTime = System.currentTimeMillis() - mStartTime;
		// if (intervalTime < MIN_INTERVAL_TIME) {
		// Toast.makeText(getContext(),
		// "时间太短！", Toast.LENGTH_SHORT).show();
		File file = new File(mFileName);
		file.delete();
		// return;
		// }

		if (mOnFinishedListerer != null)
			mOnFinishedListerer.onFinishedRecord(mIsCancel);
	}

	private void init() {
		if (mVolumeHandler == null)
			mVolumeHandler = new ShowVolumeHandler();
		if (mAudioUtil == null)
			mAudioUtil = new AudioUtil();
		initDialog();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Tools.Log("onLayout: ");
		getHotAreaRectF();
	}

	private void initDialog() {

		mStartTime = System.currentTimeMillis();

		if (mRecordDialog == null)
			mRecordDialog = new Dialog(getContext(), R.style.recording_dialog);

		View view = View.inflate(getContext(), R.layout.dialog_record_button, null);
		mVolumeViewer = (VolumeViewer) view.findViewById(R.id.vol_view);
		mVolumeIcon = (ImageView) view.findViewById(R.id.vol_icon);
		mText = (TextView) view.findViewById(R.id.text);

		mRecordDialog.setContentView(view);
		mRecordDialog.setCanceledOnTouchOutside(false);

		WindowManager.LayoutParams params = mRecordDialog.getWindow()
				.getAttributes();
		int px = Tools.dp2px(getContext(), 250);
		params.width = px;
		params.height = px;
		mRecordDialog.getWindow().setAttributes(params);

		if (onDismiss != null)
			mRecordDialog.setOnDismissListener(onDismiss);
		if (onShow != null)
			mRecordDialog.setOnShowListener(onShow);

	}

	private void getHotAreaRectF() {
		int[] location = new int[2];
		// this.getLocationOnScreen(location);
		getLocationInWindow(location);
		mXpositon = location[0];
		mYpositon = location[1];
		Tools.Log("mXpositon: " + mXpositon);
		Tools.Log("mYpositon: " + mYpositon);

		hotArea = new RectF(mXpositon, mYpositon, mXpositon
				+ this.getWidth(), mYpositon
				+ this.getHeight());
		Tools.Log("hotArea: " + hotArea);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Tools.Log("RecordButton --> onTouchEvent");
		// Tools.log("event.getRawX(): " + event.getRawX());
		// Tools.log("event.getRawY(): " + event.getRawY());
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startRecording();
				break;

			case MotionEvent.ACTION_UP:
				finishRecord();
				break;

			case MotionEvent.ACTION_MOVE:// 当手指移动到view外面，会cancel
				if (hotArea.contains(event.getRawX(), event.getRawY())) {
					normalStatus();
				} else {
					cancelStatus();
				}

				break;
		}

		return true;
	}

	private void normalStatus() {
		mIsCancel = false;
		mVolumeViewer.setVisibility(View.VISIBLE);
		mVolumeIcon.setImageResource(R.drawable.recording_btn_radio_icon);
		mText.setText("手指上滑，取消发送");
		mText.setTextColor(Color.WHITE);
	}

	private void cancelStatus() {
		mIsCancel = true;
		mVolumeViewer.setVisibility(View.GONE);
		mVolumeIcon.setImageResource(R.drawable.recording_cancel_icon);
		mText.setText("松开手指，取消发送");
		mText.setTextColor(Color.RED);
	}

	public void setSavePath(String path) {
		mFileName = AudioUtil.AUDOI_DIR + "/" + path;
		File file = new File(mFileName);
		if (!file.exists())
			file.mkdirs();
		mFileName += "/" + System.currentTimeMillis() + ".amr";
	}

	// 开始录音
	private void startRecording() {
		setSavePath("");
		mAudioUtil.setAudioPath(mFileName);
		mAudioUtil.recordAudio();
		mThread = new ObtainDecibelThread();
		mThread.start();

		if (mOnStartListerer != null)
			mOnStartListerer.onStartRecord();

		normalStatus();
		mRecordDialog.show();
	}

	private void stopRecording() {
		if (mThread != null) {
			mThread.exit();
			mThread = null;
		}
		if (mAudioUtil != null) {
			mAudioUtil.stopRecord();
		}
	}

	private class ObtainDecibelThread extends Thread {

		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mAudioUtil == null || !running) {
					break;
				}

				int volumn = mAudioUtil.getVolumn();

				if (volumn != 0)
					mVolumeHandler.sendEmptyMessage(volumn);
			}
		}

	}

	class ShowVolumeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			mVolumeViewer.setVolumeValue(msg.what);
		}
	}

}
