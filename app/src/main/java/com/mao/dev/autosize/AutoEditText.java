package com.mao.dev.autosize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * Created by Mao on 2016/10/18.
 */

public class AutoEditText extends EditText{
    private static final int ANIMATION_DURATION = 300;

    private int mLineLimit = 1;
    private boolean mResizeInProgress;
    private int mMaxWidth;

    public AutoEditText(Context context) {
        super(context);
    }

    public AutoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxWidth = w - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        float linesNeeded = calculateNumberOfLinesNeeded();
        Log.d("mpg", "linesNeed = " + linesNeeded);
        if (linesNeeded > mLineLimit) {
            zoomOutTextSize();
        } else {
            zoomInTextSize();
        }
    }

    private void zoomOutTextSize() {
        Paint measurePaint = getMeasurePaint();
        Log.d("mpg", "zoom out, textsize=" + getTextSize() + ", paintsize=" + measurePaint.getTextSize());
        float textWidth = 0;
        textWidth = measurePaint.measureText(getText().toString());
        // 先做1行处理
        while (textWidth > mMaxWidth) {
            measurePaint.setTextSize(measurePaint.getTextSize() - 1);
            textWidth = measurePaint.measureText(getText().toString());
        }
        Log.d("mpg", "textsize = " + measurePaint.getTextSize());
        playAnimation(getPaint().getTextSize(), measurePaint.getTextSize());
    }

    private void zoomInTextSize() {
        if (getPaint().getTextSize() <= getTextSize()) {
            return;
        }
        //先做一行处理
        Paint measurePaint = getMeasurePaint();
        float textWidth = 0;
        textWidth = measurePaint.measureText(getText().toString());

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
                mResizeInProgress = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mResizeInProgress = true;
            }
        });
        animator.start();
    }
}
