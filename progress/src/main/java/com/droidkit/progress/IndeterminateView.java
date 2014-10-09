package com.droidkit.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Implementation of indeterminate Progress bar view from Material Design
 */
public class IndeterminateView extends View {

    private static final int DEFAULT_COLOR = 0xff458aed;

    private static final int DEFAULT_BG_COLOR = 0xffb8cdf0;

    private int height;

    private int r;

    private static final int MAIN_DELTA = 1600;

    private static final int WIDTH_DELTA = 1100;

    private int color = DEFAULT_COLOR;
    private int backgroundColor = DEFAULT_BG_COLOR;

    private RectF rectF = new RectF();

    private Paint foregroundPaint = new Paint();
    private Paint bgPaint = new Paint();

    public IndeterminateView(Context context) {
        super(context);
        init();
    }

    public IndeterminateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndeterminateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.height = (int) (getResources().getDisplayMetrics().density * 4);
        this.r = height / 2;
        this.foregroundPaint.setColor(color);
        this.bgPaint.setColor(backgroundColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int w;
        switch (wMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                w = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                w = (int) (getResources().getDisplayMetrics().density * 100);
                break;
        }

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int h;
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                h = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                h = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                h = height;
                break;
        }

        setMeasuredDimension(w, h);
    }

    /**
     * Getting color of progress
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Setting progress color
     *
     * @param color the color
     */
    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
    }

    /**
     * Getting background color
     *
     * @return the color
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Setting background color
     *
     * @param backgroundColor the color
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.bgPaint.setColor(backgroundColor);
    }

    private float sinEasing(double val) {
        return (float) (Math.sin(2 * Math.PI * val) + 1) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int topOffset = (getHeight() - height) / 2;

        int width = getWidth();

        canvas.drawRect(0, topOffset, width, topOffset + height, bgPaint);

        int offset1 = (int) (3 * width * (System.currentTimeMillis() % MAIN_DELTA) / (float) MAIN_DELTA) - width;

        int offset2 = (int) (3 * width * ((System.currentTimeMillis() + MAIN_DELTA / 3) % MAIN_DELTA) / (float) MAIN_DELTA) - width;

        int offset3 = (int) (3 * width * ((System.currentTimeMillis() + 2 * MAIN_DELTA / 3) % MAIN_DELTA) / (float) MAIN_DELTA) - width;

        int w1 = (int) (0.5f * width * sinEasing((((System.currentTimeMillis()) % WIDTH_DELTA) / (float) WIDTH_DELTA))) +
                height * 2;

        int w2 = (int) (0.5f * width * sinEasing((((System.currentTimeMillis() + WIDTH_DELTA / 3) % WIDTH_DELTA) / (float) WIDTH_DELTA))) +
                height * 2;

        int w3 = (int) (0.5f * width * sinEasing((((System.currentTimeMillis() + 2 * WIDTH_DELTA / 3) % WIDTH_DELTA) / (float) WIDTH_DELTA))) +
                height * 2;

        rectF.set(offset1, topOffset, offset1 + w1, topOffset + height);
        canvas.drawRoundRect(rectF, r, r, foregroundPaint);

        rectF.set(offset2, topOffset, offset2 + w2, topOffset + height);
        canvas.drawRoundRect(rectF, r, r, foregroundPaint);

        rectF.set(offset3, topOffset, offset3 + w3, topOffset + height);
        canvas.drawRoundRect(rectF, r, r, foregroundPaint);

        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }
}
