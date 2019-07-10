package com.mao.dev.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/1/23.
 */

public class HalfCircleView extends View {

    private Paint mShadowPaint;
    private Path mCirclePath;

    public HalfCircleView(Context context) {
        super(context);
        init();
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HalfCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(getResources().getColor(R.color.hani_c12));
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mCirclePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int radius = w / 2 - dp2px(20);
        int centerY = dp2px(60) - radius;
        mCirclePath.addCircle(w / 2, centerY, radius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mShadowPaint.setShadowLayer(dp2px(10), 0, dp2px(2.5f), getResources().getColor(R.color.colorAccent));
        canvas.drawPath(mCirclePath, mShadowPaint);
        canvas.restore();
    }

    private int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }
}
