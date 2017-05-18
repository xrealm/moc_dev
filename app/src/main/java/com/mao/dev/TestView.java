package com.mao.dev;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mao on 2017/5/18.
 */

public class TestView extends View {

    private Paint mPaint;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(0xffff6666);
        mPaint.setPathEffect(new CornerPathEffect(30));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(100, 100);
        path.lineTo(250, 100);
        path.lineTo(250, 300);

        canvas.drawPath(path, mPaint);
    }
}
