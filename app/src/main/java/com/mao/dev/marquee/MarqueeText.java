package com.mao.dev.marquee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by Mao on 16/10/7.
 */

public class MarqueeText extends TextView {
    private static final int SCROLL_DURATION = 5000;
    private ValueAnimator mScrollAnimator;
    private ValueAnimator mRestoreAnimator;
    private boolean isScroll;

    public MarqueeText(Context context) {
        this(context, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
    }

    public void startScroll() {
        float measureText = getPaint().measureText(getText().toString());
        int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        if (measureText <= contentWidth || isScroll) {
            return;
        }
        isScroll = true;
        scrollToLeft();
    }

    private void scrollToLeft() {
        float measureText = getPaint().measureText(getText().toString());
        int duration = (int) (measureText / getWidth() * SCROLL_DURATION);
        mScrollAnimator = ValueAnimator.ofInt(0, (int) measureText);
        mScrollAnimator.setDuration(duration);
        mScrollAnimator.setInterpolator(new LinearInterpolator());
        mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scrollTo((Integer) valueAnimator.getAnimatedValue(), 0);
            }
        });
        mScrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mScrollAnimator = null;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isScroll()) {
                            playToNomal();
                        }
                    }
                }, 600);
            }
        });
        mScrollAnimator.start();
    }

    private void playToNomal() {
        if (getScrollX() <= 0) {
            return;
        }
        mRestoreAnimator = ValueAnimator.ofInt(getScrollX(), 0);
        mRestoreAnimator.setDuration(500);
        mRestoreAnimator.setInterpolator(new LinearInterpolator());
        mRestoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                scrollTo((Integer) valueAnimator.getAnimatedValue(), 0);
            }
        });
        mRestoreAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isScroll()) {
                    scrollToLeft();
                }
            }
        });
        mRestoreAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimation();
    }

    private void cancelAnimation() {
        cancelAnimator(mScrollAnimator);
        cancelAnimator(mRestoreAnimator);
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void stopScroll() {
        cancelAnimation();
        scrollTo(0, 0);
        isScroll = false;
    }

    private void cancelAnimator(ValueAnimator animator) {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
        }
    }
}

