package com.mao.dev.ui.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Mao on 1/8/18.
 */
public class PathView extends View {
    Path mPath;
    Paint mPaint;

    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
        mPath.moveTo(100, 100);
        mPath.quadTo(400,400, 800,200);
        mPath.quadTo(1000,300, 800,400);
        mPath.cubicTo(600, 500, 300, 500, 200, 1000);
        mPath.cubicTo(400, 600, 600, 1200, 800, 1000);
        mPath.lineTo(800, 1200);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);

        if (mPathPoint != null) {
            canvas.drawCircle(mPathPoint.x, mPathPoint.y, 20, mPaint);
        }
    }

    PathPoint mPathPoint;
    public void setPathPoint(PathPoint animatedValue) {
        mPathPoint = animatedValue;
        invalidate();
    }
}
