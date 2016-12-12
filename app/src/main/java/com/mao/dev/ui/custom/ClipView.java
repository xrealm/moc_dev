package com.mao.dev.ui.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mao on 2016/11/29.
 */

public class ClipView extends View {

    public ClipView(Context context) {
        super(context);
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
//        canvas.clipPath(getClipPath());
        canvas.drawColor(0x33ff0000);
        canvas.restore();
    }

    private Path getClipPath() {
        Path path = new Path();
        path.moveTo(0, getHeight() / 2);
        path.lineTo(getWidth() / 2, getHeight());
        path.lineTo(getWidth(), getHeight() / 2);
        path.lineTo(getWidth() / 2, 0);
        path.close();
        return path;
    }
}
