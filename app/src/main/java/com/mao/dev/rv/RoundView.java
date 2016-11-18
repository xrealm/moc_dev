package com.mao.dev.rv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mao on 2016/11/18.
 */

public class RoundView extends View {
    private Paint mPaint;

    public RoundView(Context context) {
        super(context);
    }

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xbfffffff);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(44);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0x44000000);
        mPaint.setColor(0xbfffffff);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, 99, 99, mPaint);
        mPaint.setColor(0xff171717);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float baseline = getHeight() / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2;
        canvas.drawText("哈尼直播", getWidth() / 2, baseline, mPaint);
    }
}
