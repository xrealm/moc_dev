package com.mao.dev.ui.progress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mao on 2017/11/10.
 */

public class CircleProgressView extends View {

    private Paint paint;
    private RectF ovalRectF;
    private int startAngle = -90;
    private float sweepAngle = 360;
    private long duration = 60_000;
    private long startTimestamp;

    private Animator timerAnimator;

    private ProgressAnimationListener animationListener;


    public CircleProgressView(Context context) {
        super(context);
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ovalRectF = new RectF(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(ovalRectF, startAngle, sweepAngle, true, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel(timerAnimator);
    }

    public void setupProgress() {
        if (timerAnimator != null) {
            timerAnimator.cancel();
        }
        timerAnimator = ObjectAnimator.ofFloat(this, "sweepAngle", 360, 0)
                .setDuration(10000);
        timerAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animationListener != null) {
                    animationListener.onAnimationEnd();
                }
            }
        });
        timerAnimator.start();

    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    private void cancel(Animator animator) {
        if (animator != null) {
            animator.cancel();
        }
    }

    public void setAnimationListener(ProgressAnimationListener listener) {
        this.animationListener = listener;
    }

    public interface ProgressAnimationListener {
        void onAnimationEnd();
    }
}
