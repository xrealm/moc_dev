package com.mao.dev.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Mao on 2016/11/29.
 */

public class ClipLayout extends FrameLayout {
    public ClipLayout(Context context) {
        super(context);
    }

    public ClipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClipLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.clipPath(getClipPath());
        super.dispatchDraw(canvas);
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
