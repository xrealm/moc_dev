package com.mao.dev.autosize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * Created by Mao on 2016/10/18.
 */

public class AutoSizeEditText extends EditText{
    private static final int ANIMATION_DURATION = 300;

    private int mLineLimit = 1;
    private boolean mIsResize;
    private int mMaxWidth;
    private float mOriginTextSize;

    public AutoSizeEditText(Context context) {
        super(context);
        init();
    }

    public AutoSizeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoSizeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoSizeEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mOriginTextSize = getTextSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxWidth = w - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mIsResize) {
            return;
        }
        float linesNeeded = calculateNumberOfLinesNeeded();
        if (linesNeeded > mLineLimit) {
            zoomOutTextSize();
        } else {
            zoomInTextSize();
        }
    }

    private void zoomOutTextSize() {
        Paint measurePaint = getMeasurePaint();
        float textWidth = 0;
        String content = getText().toString();
        textWidth = measurePaint.measureText(content);
        // 先只做1行处理
        while (textWidth > mMaxWidth) {
            measurePaint.setTextSize(measurePaint.getTextSize() - 1);
            textWidth = measurePaint.measureText(content);
        }
        playAnimation(getPaint().getTextSize(), measurePaint.getTextSize());
    }

    private void zoomInTextSize() {
        if (mOriginTextSize <= getTextSize()) {
            return;
        }
        //先只做一行处理
        float textWidth = 0;
        String content = getText().toString();
        Paint measurePaint = getMeasurePaint();
        while (measurePaint.getTextSize() < mOriginTextSize) {
            textWidth = measurePaint.measureText(content);
            if (textWidth >= mMaxWidth) {
                break;
            }
            measurePaint.setTextSize(measurePaint.getTextSize() + 1);
        }
        playAnimation(getPaint().getTextSize(), measurePaint.getTextSize());
    }

    private Paint getMeasurePaint() {
        Paint paint = new Paint();
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());
        return paint;
    }

    private float calculateNumberOfLinesNeeded() {
        float textWidth = getPaint().measureText(getText().toString());
        return textWidth / mMaxWidth;
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    private void playAnimation(float origin, float destination) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "textSize", origin, destination);
        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsResize = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsResize = true;
            }
        });
        animator.start();
    }
}
