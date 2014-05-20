/**
 * @author harry
 * @version 1
 * @date 2014/5/13
 */

package org.yeah.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.yeah.R;

/**
 * The Class CircularSeekBar.
 */
public class ArcSeekBar extends View {

    public static final String TAG = "ArcSeekBar";

    /** The context */
    private Context mContext;

    /** The listener to listen for changes */
    private OnSeekChangeListener mSeekChangeListener;

    /** The listener to listen for changes */
    private OnSwitchChangeListener mSwitchChaneListener;

    /** The paint of the progress ring */
    private Paint arcPaint;

    /** The paint of the progress text */
    private TextPaint textPaint;

    /** The width of the view */
    private int width;

    /** The height of the view */
    private int height;

    /** The maximum progress amount */
    private float maxProgress = (float) 100.0;

    /** The current progress */
    private float progress;

    /** The progress percent */
    private int progressPercent;

    /** The radius of the inner circle */
    private float innerRadius;

    /** The radius of the outer circle */
    private float outerRadius;

    /** The circle's center X coordinate */
    private float cx;

    /** The circle's center Y coordinate */
    private float cy;

    /** The left bound for the circle RectF */
    private float left;

    /** The right bound for the circle RectF */
    private float right;

    /** The top bound for the circle RectF */
    private float top;

    /** The bottom bound for the circle RectF */
    private float bottom;

    /** The X coordinate for the top left corner of the marking drawable */
    private float thumbX;

    /** The Y coordinate for the top left corner of the marking drawable */
    private float thumbY;

    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointX;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private float markPointY;

    /** The progress mark when the view isn't being progress modified */
    private Bitmap progressMark;

    /** The progress mark when the view is being progress modified. */
    private Bitmap progressMarkPressed;

    private Bitmap mSeekbarBgBitmap;

    /** The flag to see if view is pressed */
    private boolean IS_PRESSED = false;

    /**
     * The flag to see if the setProgress() method was called from our own
     * View's setAngle() method, or externally by a user.
     */
    // private boolean CALLED_FROM_ANGLE = false;

    /** The rectangle containing our circles and arcs. */
    private RectF rect = new RectF();

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public ArcSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        // TypedArray a = context.obtainStyledAttributes(attrs,
        // R.styleable.CircularSeekBar);
        // Drawable seekbarDrawble =
        // a.getDrawable(R.styleable.CircularSeekBar_seekbar_bg);
        // BitmapDrawable seekbarBgBitmapDrawable = (BitmapDrawable)
        // seekbarDrawble;
        // mSeekbarBgBitmap = seekbarBgBitmapDrawable.getBitmap();
        init();
    }

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     * @param attrs the attrs
     */
    public ArcSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        /*
         * TypedArray a = context.obtainStyledAttributes(attrs,
         * R.styleable.CircularSeekBar); Drawable seekbarDrawble =
         * a.getDrawable(R.styleable.CircularSeekBar_seekbar_bg); BitmapDrawable
         * seekbarBgBitmapDrawable = (BitmapDrawable) seekbarDrawble;
         * mSeekbarBgBitmap = seekbarBgBitmapDrawable.getBitmap();
         */
        init();
    }

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     */
    public ArcSeekBar(Context context) {
        super(context);
        init();
    }

    // Path textPath;

    /**
     * Inits the drawable.
     */
    public void init() {

        arcPaint = new Paint();
        arcPaint.setColor(Color.GREEN);
        arcPaint.setStyle(Paint.Style.STROKE);
        // arcPaint.setStrokeCap(Cap.ROUND);
        arcPaint.setAntiAlias(true);
        // arcPaint.setStrokeWidth(30);

        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#00a600"));
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setStrokeWidth(25);
        textPaint.setTextSize(45);

        // textPath = new Path();
        // textPath.moveTo(cx, cy);
        // textPath.lineTo(cx, cy);

        progressMark = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_normal_holo);
        progressMarkPressed = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_pressed_holo);
        mSeekbarBgBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.light_brightness_seekbar_bg);
    }

    private float arcRadiusAdjustInBitmap;
    private float arcRadius;
    // private float baseX = 0;
    // private float baseY = 0;

    private int viewWidth;
    private int viewHeight;

    private int getMeasuredLength(int length, boolean isWidth) {
        int specMode = MeasureSpec.getMode(length);
        int specSize = MeasureSpec.getSize(length);
        int size;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom();
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            size = isWidth ? padding + mSeekbarBgBitmap.getWidth() : mSeekbarBgBitmap
                    .getHeight() + padding;
            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }
        return size;
    }

    RadialGradient mRadialGradient;

    /*
     * (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = getMeasuredLength(widthMeasureSpec, true);
        viewHeight = getMeasuredLength(heightMeasureSpec, false);
        setMeasuredDimension(viewWidth, viewHeight);
        // Log.i(TAG, " 1111viewWidth: " + viewWidth);
        // Log.i(TAG, " 1111viewHeight: " + viewHeight);

        width = mSeekbarBgBitmap.getWidth();
        height = mSeekbarBgBitmap.getHeight();

        // baseX = (viewWidth - width) / 2;
        // baseY = (viewWidth - height) / 2;

        cx = width / 2; // Center X for circle
        cy = height / 2; // Center Y for circle
        // Log.i(TAG, " cx: " + cx);
        // Log.i(TAG, " cy: " + cy);

        // the distance of from bitmap edge to arc ring
        // 0.1818 = 140 / 770
        arcRadiusAdjustInBitmap = width * 0.1818f;
        // Radius of the arc
        arcRadius = cx - arcRadiusAdjustInBitmap;

        float arcWidthInBitmap = width * 0.1f;
        outerRadius = arcRadius + arcWidthInBitmap;
        innerRadius = arcRadius - arcWidthInBitmap;

        arcPaint.setStrokeWidth(arcWidthInBitmap);

        // Log.i(TAG, " arcRadius: " + arcRadius);

        // Log.i(TAG, " outerRadius: " + outerRadius);
        // Log.i(TAG, " innerRadius: " + innerRadius);
        // Log.i(TAG, " arcRadiusAdjust: " + arcRadiusAdjust);
        // Log.i(TAG, " arcRadius: " + arcRadius);

        // Calculate left bound of our rect
        left = arcRadiusAdjustInBitmap;
        // Calculate right bound of our rect
        right = width - arcRadiusAdjustInBitmap;
        // Calculate top bound of our rect
        top = arcRadiusAdjustInBitmap;
        // Calculate bottom bound ofour rect
        bottom = height - arcRadiusAdjustInBitmap;

        rect.set(left, top, right, bottom); // assign size to rect

        if (mRadialGradient == null) {
            int[] colors = new int[] {
                    Color.parseColor("#00d200"), Color.parseColor("#00a600")
            };
            mRadialGradient = new RadialGradient(cx, cy, arcRadius,
                    colors, null, TileMode.REPEAT);
        }

        arcPaint.setShader(mRadialGradient);
        // arcPaint.setShadowLayer(arcRadius, cx, cy, Color.RED);
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mSeekbarBgBitmap, 0, 0, null);
        // canvas.drawBitmap(mSeekbarBgBitmap, baseX, baseY, null);
        // canvas.drawRect(rect, arcPaint);
        // canvas.drawPoint(cx, cy, arcPaint);
        // canvas.drawCircle(cx, cy, outerRadius, circleRing);
        // Log.i(TAG, " angle: " + angle);
        // canvas.drawCircle(cx, cy, innerRadius, innerColor);

        canvas.drawArc(rect, startAngle, angle, false, arcPaint);
        // canvas.drawTextOnPath(getProgressPercent() + "%", textPath, -5, 5,
        // textPaint);
        canvas.drawText(getProgressPercent() + "%", cx, cy + 15, textPaint);
        // thumbX = getXFromAngle();
        // thumbY = getYFromAngle();
        // drawMarkerAtProgress(canvas);

        super.onDraw(canvas);
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        // Log.i(TAG, " onTouchEvent -->  x : " + x);
        // Log.i(TAG, " onTouchEvent -->  y : " + y);

        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // CALLED_FROM_USER = false;
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                // CALLED_FROM_USER = true;
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }

    /** The angle of progress */
    private int angle = 0;

    /**
     * 圆弧起始角度，单位为度
     */
    private int startAngle = 97;

    /** The maximum progress amount */
    private int maxAngle = 348;

    /**
     * Moved.
     * 
     * @param x the x
     * @param y the y
     * @param up the up
     */
    private void moved(float x, float y, boolean up) {

        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (!up) {
            if (distance < outerRadius && distance > innerRadius) {

                IS_PRESSED = true;

                // 1.Math.atan2(x,y)函数返回点（x,y)和原点(0,0)之间连线的倾斜角，所以可以用它实现计算出两点间连线的夹角
                // 或者說实现直角坐标系向极坐标系的转换。
                // 2.toDegrees(): 2 * Math.PI
                float degrees = (float) ((Math.toDegrees(Math.atan2(y - cy, x - cx)) + 260) % 360);
                Log.i("harry", " moved -->  degrees1111 : " + degrees);
                if (degrees < 0 || degrees > maxAngle) {
                    return;
                }

                setAngle(Math.round(degrees));
            }
        } else {
            /*
             * if (distance <= centerSwitchBtnRadius) { mIsSwitchOn =
             * mIsSwitchOn ? false : true; Log.i(TAG,
             * " moved -->  mIsSwitchOn : " + mIsSwitchOn); if
             * (mSwitchChaneListener != null)
             * playSoundEffect(SoundEffectConstants.CLICK);
             * mSwitchChaneListener.onSwitchChange(this, mIsSwitchOn); return; }
             */

            Log.i(TAG, " moved -->  mSeekChangeListener.onProgressChange");
            if (mSeekChangeListener != null)
                mSeekChangeListener.onProgressChange(this, this.progress);
            IS_PRESSED = false;
            invalidate();
        }

    }

    /**
     * Draw marker at the current progress point onto the given canvas.
     * 
     * @param canvas the canvas
     */
    public void drawMarkerAtProgress(Canvas canvas) {
        if (IS_PRESSED) {
            canvas.drawBitmap(progressMarkPressed, thumbX, thumbY, null);
        } else {
            canvas.drawBitmap(progressMark, thumbX, thumbY, null);
        }
    }

    /**
     * Gets the X coordinate of the arc's end arm's point of intersection with
     * the circle
     * 
     * @return the X coordinate
     */
    public float getXFromAngle() {
        int size1 = progressMark.getWidth();
        int size2 = progressMarkPressed.getWidth();
        int adjust = (size1 > size2) ? size1 : size2;
        markPointX = (float) (arcRadius * Math.cos(Math.toRadians(angle + 180)) + cx);
        // Log.i(TAG, " getXFromAngle -->  markPointX : " + markPointX);
        float x = markPointX - (adjust / 2);
        return x;
    }

    /**
     * Gets the Y coordinate of the arc's end arm's point of intersection with
     * the circle
     * 
     * @return the Y coordinate
     */
    public float getYFromAngle() {
        int size1 = progressMark.getHeight();
        int size2 = progressMarkPressed.getHeight();
        int adjust = (size1 > size2) ? size1 : size2;
        markPointY = (float) (arcRadius * Math.sin(Math.toRadians(angle + 180)) + cy);
        // Log.i(TAG, " getYFromAngle -->  markPointY : " + markPointY);
        float y = markPointY - (adjust / 2);
        return y;
    }

    /**
     * Set the angle.
     * 
     * @param angle the new angle
     */
    private void setAngle(int angle) {
        // Log.i(TAG, "setAngle: " + angle);
        this.angle = angle;
        float donePercent = (((float) Math.abs(this.angle)) / maxAngle) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        // CALLED_FROM_ANGLE = true;
        // setProgressPercent(Math.round(donePercent));
        this.progressPercent = Math.round(donePercent);
        // setProgress(Math.round(progress), false);
        this.progress = Math.round(progress);
        this.invalidate();
    }

    /**
     * Gets the max progress.
     * 
     * @return the max progress
     */
    public float getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the max progress.
     * 
     * @param maxProgress the new max progress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * Gets the progress.
     * 
     * @return the progress
     */
    public float getProgress() {
        return progress;
    }

    // private boolean CALLED_FROM_USER = true;

    /**
     * Sets the progress.
     * 
     * @param progress the new progress.
     * @param isCallSeekChangeListener if call the seek change listener.
     */
    public void setProgress(int progress, boolean isCallSeekChangeListener) {
        Log.i(TAG, "setProgress: " + progress);
        if (this.progress != progress) {
            this.progress = progress;
            // if (!CALLED_FROM_ANGLE) {
            float newPercent = (this.progress / this.maxProgress) * 100;
            // setProgressPercent((int) newPercent);
            Log.i(TAG, "newPercent: " + newPercent);
            this.progressPercent = (int) newPercent;
            Log.i(TAG, "progressPercent: " + progressPercent);
            float newAngle = (newPercent / 100) * maxAngle;
            this.setAngle(Math.round(newAngle));
            // }
            if (mSeekChangeListener != null && isCallSeekChangeListener) {
                mSeekChangeListener.onProgressChange(this, this.progress);
            }
            // CALLED_FROM_ANGLE = false;
            // this.invalidate();
        }
    }

    /**
     * Gets the progress percent.
     * 
     * @return the progress percent
     */
    public int getProgressPercent() {
        Log.i(TAG, " progressPercent: " + progressPercent);
        return progressPercent;
    }

    /**
     * Sets the progress percent.
     * 
     * @param progressPercent the new progress percent
     * @param isCallSeekChangeListener if call the seek change listener.
     */
    public void setProgressPercent(int newProgressPercent, boolean isCallSeekChangeListener) {
        Log.i(TAG, "setProgressPercent: " + newProgressPercent);
        setProgress(Math.round((float) ((float) newProgressPercent / 100.0) * maxProgress),
                isCallSeekChangeListener);
    }

    /**
     * The listener interface for receiving onSeekChange events. The class that
     * is interested in processing a onSeekChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * <code>setSeekBarChangeListener(OnSeekChangeListener)<code> method. When
     * the onSeekChange event occurs, that object's appropriate
     * method is invoked.
     * 
     * @see OnSeekChangeEvent
     */
    public interface OnSeekChangeListener {

        /**
         * On progress change.
         * 
         * @param view the view
         * @param newProgress the new progress
         */
        public void onProgressChange(ArcSeekBar view, float newProgress);
    }

    /**
     * Sets the seek bar change listener.
     * 
     * @param listener the new seek bar change listener
     */
    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        mSeekChangeListener = listener;
    }

    /**
     * Gets the seek bar change listener.
     * 
     * @return the seek bar change listener
     */
    public OnSeekChangeListener getOnSeekChangeListener() {
        return mSeekChangeListener;
    }

    /**
     * * The listener interface for receiving onSwitchChange events.
     */
    public interface OnSwitchChangeListener {

        /**
         * On Switch change.
         * 
         * @param view the view
         * @param SwitchStatus the new SwitchStatus
         */
        public void onSwitchChange(ArcSeekBar view, boolean SwitchStatus);
    }

    /**
     * Sets the Switch change listener.
     * 
     * @param listener the new Switch status listener
     */
    public void setOnSwitchChangeListener(OnSwitchChangeListener switchChangeListener) {
        mSwitchChaneListener = switchChangeListener;
    }

    /**
     * Gets the Switch change listener.
     * 
     * @return the Switch change listener
     */
    public OnSwitchChangeListener getOnSwitchChangeListener() {
        return mSwitchChaneListener;
    }

}
