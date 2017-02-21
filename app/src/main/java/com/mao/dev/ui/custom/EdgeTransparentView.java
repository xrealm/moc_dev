package com.mao.dev.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
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

import com.mao.dev.R;

/**
 * Created by Mao on 2017/2/7.
 */

public class EdgeTransparentView extends FrameLayout {
    private static int EDGE_TOP = 0x1;
    private static int EDGE_BOTTOM = 0x1 << 1;
    private static int EDGE_LEFT = 0x1 << 2;
    private static int EDGE_RIGHT = 0x1 << 3;

    private Paint mPaint;
    private int mPosition;
    private float mDrawSize;
    private int[] mGradientColors = {0xffffffff, 0x00000000};
    private float[] mGradientPositions = {0, 1};

    public EdgeTransparentView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EdgeTransparentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EdgeTransparentView);
        mPosition = typedArray.getInt(R.styleable.EdgeTransparentView_position, 0);
        mDrawSize = typedArray.getDimension(R.styleable.EdgeTransparentView_edge_size, dp2px(20));
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
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
        if (mPosition == 0 || (mPosition & EDGE_TOP) != 0) {
            drawTop(canvas);
        }
        //bottom
        if (mPosition == 0 || (mPosition & EDGE_BOTTOM) != 0) {
            drawBottom(canvas);
        }

        //left
        if (mPosition == 0 || (mPosition & EDGE_LEFT) != 0) {
            drawLeft(canvas);
        }
        //bottom
        if (mPosition == 0 || (mPosition & EDGE_RIGHT) != 0) {
            drawRight(canvas);
        }
        canvas.restoreToCount(layer);
        return drawChild;
    }

    private void drawRight(Canvas canvas) {
        int layerCount = canvas.save();
        int offset = (getHeight() - getWidth()) / 2;
        canvas.rotate(270, getWidth() / 2, getHeight() / 2);
        canvas.translate(0, offset);
        canvas.drawRect(-offset, 0, getWidth() + offset, mDrawSize, mPaint);
        canvas.restoreToCount(layerCount);
    }

    private void drawLeft(Canvas canvas) {
        int layerCount = canvas.save();
        int offset = (getHeight() - getWidth()) / 2;
        canvas.rotate(90, getWidth() / 2, getHeight() / 2);
        canvas.translate(0, offset);
        canvas.drawRect(-offset, 0, getWidth() + offset, mDrawSize, mPaint);
        canvas.restoreToCount(layerCount);
    }

    private void drawBottom(Canvas canvas) {
        int layerCount = canvas.save();
        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        // -1...
        canvas.drawRect(0, -1, getWidth(), mDrawSize, mPaint);
        canvas.restoreToCount(layerCount);
    }

    private void drawTop(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), mDrawSize, mPaint);
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
