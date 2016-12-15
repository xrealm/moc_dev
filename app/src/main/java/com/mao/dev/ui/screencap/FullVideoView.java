package com.mao.dev.ui.screencap;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.mao.dev.AppKit;

/**
 * Created by Mao on 2016/12/15.
 */

public class FullVideoView extends VideoView {

    private int mVideoWidth;
    private int mVideoHeight;

    public FullVideoView(Context context) {
        super(context);
    }

    public FullVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            setMeasuredDimension(mVideoWidth, mVideoHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void layoutDisplay(int videoWidth, int videoHeight, int targetWidth, int targetHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }
        float videoRadio = videoWidth / (float) videoHeight;
        float windowRadio = targetWidth / (float) targetHeight;

        int width = targetWidth;
        int height = targetHeight;

        if (videoRadio < windowRadio) {
            width = targetWidth;
            height = (int) (width / videoRadio);
        } else {
            height = targetHeight;
            width = (int) (height * videoRadio);
        }
        int x = (targetWidth - width) / 2;
        int y = (targetHeight - height) / 2;

        if (getLeft() != x || getTop() != y || getWidth() != width || getHeight() != height) {
            mVideoWidth = width;
            mVideoHeight = height;
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
            layout(x, y, mVideoWidth, mVideoHeight);
        }
    }

}
