/**
 * @author harry
 * @version 5
 * @date 2013/12/06
 */

package org.yeah.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.yeah.R;

/**
 * The Class CircularSeekBar.
 */
public class CircularSeekBar2 extends View {

    public static final String TAG = "harry";

    /** The context */
    private Context mContext;

    /** The listener to listen for changes */
    private OnSeekChangeListener mListener;

    /** The color of the progress ring */
    private Paint circleColor;

    /** the color of the inside circle. Acts as background color */
    private Paint innerColor;

    /** The progress circle ring background */
    private Paint circleRing;

    /** The progress percent text paint */
    private TextPaint progressPercentTextPaint;

    /** The logo text paint */
    private TextPaint logoTextPaint;

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
    private float dx;

    /** The Y coordinate for the top left corner of the marking drawable */
    private float dy;

    /** The X coordinate for 12 O'Clock */
    private float startPointX;

    /** The Y coordinate for 12 O'Clock */
    private float startPointY;

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
    private boolean CALLED_FROM_ANGLE = false;

    /** The rectangle containing our circles and arcs. */
    private RectF rect = new RectF();

    {
        mListener = new OnSeekChangeListener() {

            @Override
            public void onProgressChange(CircularSeekBar2 view, float newProgress) {

            }
        };

        circleColor = new Paint();
        innerColor = new Paint();
        circleRing = new Paint();

        // circleColor.setColor(Color.parseColor("#ff33b5e5"));
        circleColor.setColor(Color.BLUE);
        circleColor.setStyle(Paint.Style.STROKE);

        innerColor.setColor(Color.BLACK);
        circleRing.setColor(Color.RED);

        circleColor.setAntiAlias(true);
        innerColor.setAntiAlias(true);
        circleRing.setAntiAlias(true);

        circleColor.setStrokeWidth(12);
        innerColor.setStrokeWidth(5);
        circleRing.setStrokeWidth(5);

        progressPercentTextPaint = new TextPaint();
        progressPercentTextPaint.setColor(Color.WHITE);
        progressPercentTextPaint.setAntiAlias(true);
        progressPercentTextPaint.setStrokeWidth(30);
        progressPercentTextPaint.setTextSize(90);

        logoTextPaint = new TextPaint();
        logoTextPaint.setColor(Color.GRAY);
        logoTextPaint.setAntiAlias(true);
        logoTextPaint.setStrokeWidth(15);
        logoTextPaint.setTextSize(30);
    }

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public CircularSeekBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
/*        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularSeekBar);
        Drawable seekbarDrawble = a.getDrawable(R.styleable.CircularSeekBar_seekbar_bg);
        BitmapDrawable seekbarBgBitmapDrawable = (BitmapDrawable) seekbarDrawble;
        mSeekbarBgBitmap = seekbarBgBitmapDrawable.getBitmap();*/
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     * @param attrs the attrs
     */
    public CircularSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        /*
         * TypedArray a = context.obtainStyledAttributes(attrs,
         * R.styleable.CircularSeekBar); Drawable seekbarDrawble =
         * a.getDrawable(R.styleable.CircularSeekBar_seekbar_bg); BitmapDrawable
         * seekbarBgBitmapDrawable = (BitmapDrawable) seekbarDrawble;
         * mSeekbarBgBitmap = seekbarBgBitmapDrawable.getBitmap();
         */
        initDrawable();
    }

    /**
     * Instantiates a new circular seek bar.
     * 
     * @param context the context
     */
    public CircularSeekBar2(Context context) {
        super(context);
        initDrawable();
    }

    /**
     * Inits the drawable.
     */
    public void initDrawable() {
        progressMark = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_normal_holo);
        progressMarkPressed = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.scrubber_control_pressed_holo);
    }

    private float arcRadiusAdjust;
    private float baseX = 0;
    private float baseY = 0;

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

    /** The width of the progress ring */
    private int barWidth = 20;

    private float arcRadius;

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

        baseX = (viewWidth - width) / 2;
        baseY = (viewWidth - height) / 2;

        // Log.i(TAG, " baseX: " + cy);
        // Log.i(TAG, " baseX: " + cy);

        // int size = (width > height) ? height : width;

        cx = baseX + (width / 2); // Center X for circle
        cy = baseY + (height / 2); // Center Y for circle

        // Log.i(TAG, " cx: " + cx);
        // Log.i(TAG, " cy: " + cy);

        outerRadius = width / 2; // Radius of the outer circle
        arcRadiusAdjust = width / 18;

        // Log.i(TAG, " arcRadiusAdjust: " + arcRadiusAdjust);

        barWidth = width / 9;

        // Radius of the inner circle
        innerRadius = outerRadius - barWidth;

        // Radius of the arc
        arcRadius = outerRadius - arcRadiusAdjust;
        // Log.i(TAG, " arcRadius: " + arcRadius);

        // Calculate left bound of our rect
        left = baseX + arcRadiusAdjust;
        // Calculate right bound of our rect
        right = baseX + width - arcRadiusAdjust;
        // Calculate top bound of our rect
        top = baseY + arcRadiusAdjust;
        // Calculate bottom bound ofour rect
        bottom = baseY + height - arcRadiusAdjust;

        // startPointX = cx - (float) Math.cos(35) * arcRadius;
        // //startPointY = cy + (float) Math.sin(35) * arcRadius;
        //startPointX = baseX + arcRadiusAdjust + 35;
        //startPointY = cy + 110;

        // Initial locatino of the marker X coordinate
        //markPointX = startPointX;
        // Initial locatino of the marker Y coordinate
        //markPointY = startPointY;

        rect.set(left, top, right, bottom); // assign size to rect
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        
        canvas.drawBitmap(mSeekbarBgBitmap, baseX, baseY, null);
        // canvas.drawRect(rect, circleRing);
        // canvas.drawPoint(cx, cy, innerColor);

        // canvas.drawCircle(cx, cy, outerRadius, circleRing);
        canvas.drawArc(rect, startAngle, angle, false, circleColor);
        // Log.i(TAG, " angle: " + angle);
        // canvas.drawCircle(cx, cy, innerRadius, innerColor);
        
        dx = getXFromAngle();
        dy = getYFromAngle();
        drawMarkerAtProgress(canvas);

        // canvas.drawTextOnPath(progressPercent + "%", new Path, hOffset,
        // vOffset, progressPercentTextPaint);
        // canvas.drawText(progressPercent + "%", width / 3, cy,
        // progressPercentTextPaint);
        // canvas.drawText("SmartHost", width / 3, height * 2 / 3,
        // logoTextPaint);

        super.onDraw(canvas);
    }

    /**
     * Draw marker at the current progress point onto the given canvas.
     * 
     * @param canvas the canvas
     */
    public void drawMarkerAtProgress(Canvas canvas) {
        if (IS_PRESSED) {
            canvas.drawBitmap(progressMarkPressed, dx, dy, null);
        } else {
            canvas.drawBitmap(progressMark, dx, dy, null);
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
        markPointX = (float) (arcRadius * Math.cos(Math.toRadians(angle + 145)) + cx);
        //Log.i(TAG, " getXFromAngle -->  markPointX : " + markPointX);
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
        markPointY = (float) (arcRadius * Math.sin(Math.toRadians(angle + 145)) + cy);
        //Log.i(TAG, " getYFromAngle -->  markPointY : " + markPointY);
        float y = markPointY - (adjust / 2);
        return y;
    }

    /**
     * Get the angle.
     * 
     * @return the angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Set the angle.
     * 
     * @param angle the new angle
     */
    public void setAngle(int angle) {
        Log.i(TAG, "setAngle: " + angle);
        this.angle = angle;
        float donePercent = (((float) this.angle) / maxAngle) * 100;
        float progress = (donePercent / 100) * getMaxProgress();
        CALLED_FROM_ANGLE = true;
        setProgressPercent(Math.round(donePercent));
        setProgress(Math.round(progress));
    }

    /**
     * Sets the seek bar change listener.
     * 
     * @param listener the new seek bar change listener
     */
    public void setSeekBarChangeListener(OnSeekChangeListener listener) {
        mListener = listener;
    }

    /**
     * Gets the seek bar change listener.
     * 
     * @return the seek bar change listener
     */
    public OnSeekChangeListener getSeekBarChangeListener() {
        return mListener;
    }

    /**
     * Gets the bar width.
     * 
     * @return the bar width
     */
    public int getBarWidth() {
        return barWidth;
    }

    /**
     * Sets the bar width.
     * 
     * @param barWidth the new bar width
     */
    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
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
        public void onProgressChange(CircularSeekBar2 view, float newProgress);
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

    /**
     * Sets the progress.
     * 
     * @param progress the new progress
     */
    public void setProgress(int progress) {
        Log.i(TAG, "setProgress: " + progress);
        if (this.progress != progress) {
            this.progress = progress;
            if (!CALLED_FROM_ANGLE) {
                float newPercent = (this.progress / this.maxProgress) * 100;
                setProgressPercent((int) newPercent);
                float newAngle = (newPercent / 100) * maxAngle;
                this.setAngle((int) newAngle);
            }
            mListener.onProgressChange(this, this.progress);
            CALLED_FROM_ANGLE = false;
            this.invalidate();
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
     */
    public void setProgressPercent(int newProgressPercent) {
        Log.i(TAG, "setProgressPercent: " + newProgressPercent);
        this.progressPercent = newProgressPercent;
    }

    /**
     * Sets the ring background color.
     * 
     * @param color the new ring background color
     */
    public void setRingBackgroundColor(int color) {
        circleRing.setColor(color);
    }

    /**
     * Sets the back ground color.
     * 
     * @param color the new back ground color
     */
    public void setBackGroundColor(int color) {
        innerColor.setColor(color);
    }

    /**
     * Sets the progress color.
     * 
     * @param color the new progress color
     */
    public void setProgressColor(int color) {
        circleColor.setColor(color);
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mIsEnable) {
            return true;
        }
        float x = event.getX();
        float y = event.getY();

        // Log.i(TAG, " onTouchEvent -->  x : " + x);
        // Log.i(TAG, " onTouchEvent -->  y : " + y);

        boolean up = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_MOVE:
                moved(x, y, up);
                break;
            case MotionEvent.ACTION_UP:
                up = true;
                moved(x, y, up);
                break;
        }
        return true;
    }

    private boolean mIsEnable = true;

    public void setEnable(boolean isEnable) {
        mIsEnable = isEnable;
    }

    public boolean getEnable(boolean isEnable) {
        return mIsEnable;
    }

    /** The angle of progress */
    private int angle = 0;

    /** The start angle (12 O'clock */
    private int startAngle = 145;

    /** The maximum progress amount */
    private int maxAngle = 250;

    /**
     * The adjustment factor. This adds an adjustment of the specified size to
     * both sides of the progress bar, allowing touch events to be processed
     * more user friendlily (yes, I know that's not a word)
     */
    private float adjustmentFactor = 50;

    /**
     * Moved.
     * 
     * @param x the x
     * @param y the y
     * @param up the up
     */
    private void moved(float x, float y, boolean up) {

        float distance = (float) Math.sqrt(Math.pow((x - cx), 2) + Math.pow((y - cy), 2));
        if (distance < outerRadius && distance > innerRadius && !up) {
            IS_PRESSED = true;

            // float degrees = (float) ((float) ((((Math.toDegrees(Math.atan2(x
            // - cx, cy - y)) + 360.0)) % 360.0) + 125) % 360.0) ;
            // float degrees = (float) ((float) (Math.toDegrees(Math.atan2(x -
            // cx, cy - y)) + 125.0) % 360);
            float degrees = (float) ((Math.toDegrees(Math.atan2(y - cy, x - cx)) + 215) % 360);

            // Log.i(TAG, " moved -->  degrees1111 : " + degrees);

            if (degrees < 0 || degrees > maxAngle) {
                // degrees += 2 * Math.PI;
                return;
            }

            // float rate = arcRadius / distance;
            // Log.i(TAG, " moved -->  cx - left : " + (cx - left));
            // Log.i(TAG, " moved -->  arcRadius : " + arcRadius);
            // Log.i(TAG, " moved -->  distance : " + distance);
            // Log.i(TAG, " moved -->  rate : " + rate);

            // markPointX = cx - (cx - x) * rate;
            // markPointY = cy - (cy - y) * rate;

            setAngle(Math.round(degrees));

        } else {
            IS_PRESSED = false;
            invalidate();
        }

    }

    /**
     * Gets the adjustment factor.
     * 
     * @return the adjustment factor
     */
    public float getAdjustmentFactor() {
        return adjustmentFactor;
    }

    /**
     * Sets the adjustment factor.
     * 
     * @param adjustmentFactor the new adjustment factor
     */
    public void setAdjustmentFactor(float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

}
