package com.mao.dev.ui.rv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mao.dev.AppKit;

/**
 * Created by Mao on 2016/11/18.
 */

public class RoundView extends View {
    private Paint mPaint;

    private String text = "哈尼打法的基发来看打飞机";
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Rect rect = new Rect();
        mPaint.getTextBounds(text,0,text.length(),rect);
        setMeasuredDimension((int) (mPaint.measureText(text) + AppKit.dp2px(8)), rect.height()+AppKit.dp2px(2));
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xbfffffff);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(AppKit.dp2px(10));
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
        canvas.drawText(text, getWidth() / 2, baseline, mPaint);
    }

}
