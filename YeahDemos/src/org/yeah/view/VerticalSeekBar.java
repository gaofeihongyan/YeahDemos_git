
package org.yeah.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

    private Drawable mThumb;
    private Context mContext;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar view, int progress,
                boolean fromUser);

        void onStartTrackingTouch(VerticalSeekBar view);

        void onStopTrackingTouch(VerticalSeekBar view);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    public VerticalSeekBar(Context context) {
        this(context, null);
        mContext = context;
        init();
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
        mContext = context;
        init();
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;
    }

    public OnSeekBarChangeListener getOnSeekBarChangeListener() {
        return mOnSeekBarChangeListener;
    }

    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    // @Override
    // void onProgressRefresh(float scale, boolean fromUser) {
    //
    // Drawable thumb = mThumb;
    // if (thumb != null) {
    // setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
    // invalidate();
    // }
    // if (mOnSeekBarChangeListener != null && mIsUp) {
    // mOnSeekBarChangeListener.onProgressChanged(this, getProgress(),
    // fromUser);
    // }
    // }

    private Bitmap seekbar_bg;
    private int bg_x;
    private int bg_y;

    public void init() {
        // seekbar_bg = BitmapFactory.decodeResource(mContext.getResources(),
        // R.drawable.fragment_player_volume_progress_bar_bg);
        // bg_x = mThumb + mThumb.getIntrinsicWidth() / 2;
    }

    // private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
    // int available = w + getPaddingLeft() - getPaddingRight();
    // int thumbWidth = thumb.getIntrinsicWidth();
    // int thumbHeight = thumb.getIntrinsicHeight();
    // available -= thumbWidth;
    // // The extra space for the thumb to move on the track
    // available += getThumbOffset() * 2;
    //
    // int thumbPos = (int) (scale * available);
    // int topBound, bottomBound;
    // if (gap == Integer.MIN_VALUE) {
    // Rect oldBounds = thumb.getBounds();
    // topBound = oldBounds.top;// topBound:0
    // bottomBound = oldBounds.bottom;// bottomBound:45
    // } else {
    // topBound = gap;
    // bottomBound = gap + thumbHeight;
    // }
    // // int left, int top, int right, int bottom
    // thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
    //
    // }

    @Override
    protected void onDraw(Canvas c) {
        // c.drawBitmap(seekbar_bg, bg_x, bg_y, null);
        c.rotate(-90);
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
            int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    public void setThumb(Drawable thumb) {
        mThumb = thumb;
        super.setThumb(thumb);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Log.d("harry", "ACTION_DOWN===");
                setPressed(true);
                trackTouchEvent(event);
                onStartTrackingTouch();
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_MOVE:
                // Log.d("harry", "ACTION_MOVE===");
                trackTouchEvent(event);
                attemptClaimDrag();
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_UP:
                // Log.d("harry", "ACTION_UP===");
                setPressed(false);
                trackTouchEvent(event);
                onStopTrackingTouch();
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                // Log.d("harry", "ACTION_CANCEL===");
                setPressed(false);
                onStopTrackingTouch();
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        final int Height = getHeight();
        final int available = Height - getPaddingBottom() - getPaddingTop();
        int Y = (int) event.getY();
        float scale;
        float progress = 0;
        if (Y > Height - getPaddingBottom()) {
            scale = 0.0f;
        } else if (Y < getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = (float) (Height - getPaddingBottom() - Y)
                    / (float) available;
        }

        final int max = getMax();
        progress = scale * max;
        // Log.d("harry", "progress:  " + progress);
        setProgress((int) progress);
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), true);
        }
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
}
