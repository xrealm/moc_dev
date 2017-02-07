package com.mao.dev.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Mao on 2017/2/7.
 */

public class EdgeTransparentView extends FrameLayout {
    private int top = 1;
    private int bottom = 1 << 1;
    private int left = 1 << 2;
    private int right = 1 << 3;

    private Paint mPaint;
    private int mPosition;
    private float mDrawSize;
    private int[] mGradientColors = {0xffffffff, 0x00000000};
    private float[] mGradientPositions = {0, 1};

    public EdgeTransparentView(@NonNull Context context) {
        super(context);
        init();
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mDrawSize = dp2px(20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint.setShader(new LinearGradient(0, 0, 0, mDrawSize, mGradientColors, mGradientPositions, Shader.TileMode.CLAMP));
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        //top
        canvas.drawRect(0, 0, getWidth(), mDrawSize, mPaint);
        //bottom
        int save = canvas.save();
        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        canvas.drawRect(0, 0, getWidth(), mDrawSize, mPaint);
        canvas.restoreToCount(layer);
        return drawChild;
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
