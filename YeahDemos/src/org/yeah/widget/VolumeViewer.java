
package org.yeah.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class VolumeViewer extends View {

	private final static String TAG = "VolumeViewer";
	private boolean mIsFresh = true;
	private Paint mPaint;
	private int mVolumeValue = 0;

	public VolumeViewer(Context context) {
		super(context);
		init(context);
	}

	public VolumeViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	// 初始化
	private void init(Context context) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mVolumeValue = 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 根据应显示的格数画指示器
		final int height = getHeight();
		// Log.d(TAG, "height:" + height);
		for (int i = 1; i <= mVolumeValue; i++) {
			int top = height - i * 15;// 矩形间的距离 20-12
			canvas.drawRect(0, top, 10 + i * 5, top + 10, mPaint);// 矩形宽等差增加
		}

		if (mIsFresh) {
			// postInvalidateDelayed(10);
			invalidate();
		}

	}

	private int getMeasuredLength(int length, boolean isWidth) {
		int specMode = MeasureSpec.getMode(length);
		int specSize = MeasureSpec.getSize(length);
		int size;
		int padding = isWidth ? getPaddingLeft() + getPaddingRight()
				: getPaddingTop() + getPaddingBottom();
		if (specMode == MeasureSpec.EXACTLY) {
			size = specSize;
		} else {
			size = isWidth ? padding + this.getWidth() : this.getHeight() + padding;
			if (specMode == MeasureSpec.AT_MOST) {
				size = Math.min(size, specSize);
			}
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int viewWidth = getMeasuredLength(widthMeasureSpec, true);
//		int viewHeight = getMeasuredLength(heightMeasureSpec, false);
//		setMeasuredDimension(viewWidth, viewHeight);
	}

	/**
	 * 设置音量大小，并更新UI
	 * <p>
	 * 设置音量大小，并更新UI
	 * </p>
	 * 
	 * @param volume 音量大小
	 * @throws
	 */
	public void setVolumeValue(int volume) {
		// Log.d("VolumeViewer", "volume is " + volume);
		this.mVolumeValue = volume;
		if (!mIsFresh)
			mIsFresh = true;
	}

	/**
	 * 停止更新图画
	 * <p>
	 * 停止更新图画
	 * </p>
	 * 
	 * @throws
	 */
	public void stopFresh() {
		mIsFresh = false;
	}
}
