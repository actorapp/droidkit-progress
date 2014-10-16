package com.droidkit.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import com.droidkit.progress._internal.MaterialInterpolator;

/**
 * Created by ex3ndr on 16.10.14.
 */
public class CircularView extends View {
    private static int DEFAULT_COLOR = 0xff3d88f3;

    private Interpolator progressInterpolator = new MaterialInterpolator();
    private static final int ANIMATION_DURATION = 300;

    private static final int ROTATION_SPEED = 1600;

    private int w;
    private int color;
    private boolean useRotation = true;
    private Paint paint = new Paint();

    private int maxValue = 100;
    private int value = 0;

    private long lastValueChange = 0;
    private float rawValue = 0;
    private float startRawValue = 0;
    private float endRawValue = 0;

    public CircularView(Context context) {
        super(context);

        init();
    }

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CircularView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        color = DEFAULT_COLOR;
        w = (int) (getResources().getDisplayMetrics().density * 3);
        paint.setAntiAlias(true);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(w);
    }

    public void setStrokeWidth(int width) {
        this.w = width;
        invalidate();
    }

    public int getStrokeWidth() {
        return w;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public boolean isUseRotation() {
        return useRotation;
    }

    public void setUseRotation(boolean useRotation) {
        this.useRotation = useRotation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

        lastValueChange = SystemClock.uptimeMillis();
        startRawValue = rawValue;
        endRawValue = value / (float) maxValue;

        invalidate();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int r = (getWidth() - w * 4) / 2;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double angle = useRotation ? (2 * Math.PI * (SystemClock.uptimeMillis() % ROTATION_SPEED) / (float) ROTATION_SPEED) : -Math.PI / 2;
        long animationTime = SystemClock.uptimeMillis() - lastValueChange;

        boolean isAnimated;
        if (animationTime < ANIMATION_DURATION) {
            float progress = progressInterpolator.getInterpolation(animationTime / (float) ANIMATION_DURATION);
            rawValue = startRawValue + (endRawValue - startRawValue) * progress;
            isAnimated = true;
        } else {
            rawValue = endRawValue;
            isAnimated = false;
        }

        double angle2 = rawValue * 2 * Math.PI;

        float startX = (float) Math.cos(angle) * r;
        float startY = (float) Math.sin(angle) * r;

        float endX = (float) Math.cos(angle + angle2) * r;
        float endY = (float) Math.sin(angle + angle2) * r;

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX + startX, centerY + startY, w >> 1, paint);
        canvas.drawCircle(centerX + endX, centerY + endY, w >> 1, paint);

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(centerX - r, centerY - r, centerX + r, centerY + r),
                (float) (180 * angle / Math.PI), (float) (180 * angle2 / Math.PI), false, paint);

        if (isAnimated || useRotation) {
            if (Build.VERSION.SDK_INT >= 16) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }
}
