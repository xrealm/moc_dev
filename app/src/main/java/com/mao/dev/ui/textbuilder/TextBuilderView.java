package com.mao.dev.ui.textbuilder;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;

import com.facebook.fbui.textlayoutbuilder.TextLayoutBuilder;
import com.mao.dev.AppKit;

/**
 * Created by Mao on 2016/12/24.
 */

public class TextBuilderView extends View {

    private Layout mLayout;

    public TextBuilderView(Context context) {
        this(context, null);
    }

    public TextBuilderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextBuilderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextBuilderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        TextLayoutBuilder builder = new TextLayoutBuilder()
                .setText("TextLayoutBuilder makes life easy")
                .setTextSize(AppKit.dp2px(20))
                .setWidth(TextLayoutBuilder.MEASURE_MODE_AT_MOST);

        mLayout = builder.build();
    }

    public void setLayout(Layout layout) {
        mLayout = layout;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLayout.draw(canvas);
    }
}
