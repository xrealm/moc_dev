package com.mao.dev.ui.screencap;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.mao.dev.AppKit;
import com.mao.dev.R;

/**
 * Created by Mao on 2016/12/7.
 */

public class PlayScreenRecordActivity extends AppCompatActivity {

    public static final String KEY_VIDEO_PATH = "key_video_path";
    public static final String KEY_VIDEO_THUMB_PATH = "key_video_thumb_path";

    private ImageView mVideoThumb;
    private VideoView mVideoView;

    private String mVideoPath, mThumbPath;
    private int mLastPostion;
    private boolean isStoped;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hani_activity_play_screen_record);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.vv_play_record);
        mVideoThumb = (ImageView) findViewById(R.id.iv_screen_record_thumb);
        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        params.width = AppKit.getScreenWidth();
        params.height = AppKit.getScreenHeight();
        mVideoView.setLayoutParams(params);
    }

    private void initData() {
        try {
            mVideoPath = getIntent().getStringExtra(KEY_VIDEO_PATH);
            mThumbPath = getIntent().getStringExtra(KEY_VIDEO_THUMB_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(mThumbPath)) {
            mVideoThumb.setImageBitmap(BitmapFactory.decodeFile(mThumbPath));
        }
    }

    private void initEvent() {
        mVideoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoThumb.setVisibility(View.GONE);
                playVideo();
            }
        });
    }

    private void playVideo() {
        if (!TextUtils.isEmpty(mVideoPath)) {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mLastPostion > 0) {
                        mVideoView.seekTo(mLastPostion);
                    }
                }
            });
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("mpg", "onTouchEvent action=" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!mVideoView.isPlaying()) {
                mVideoView.start();
            } else {
                mVideoView.pause();
            }
        }
        return super.onTouchEvent(event);
    }

    public static void launch(Context context, String path, String thumbPath) {
        Intent intent = new Intent(context, PlayScreenRecordActivity.class);
        intent.putExtra(KEY_VIDEO_PATH, path);
        intent.putExtra(KEY_VIDEO_THUMB_PATH, thumbPath);
        context.startActivity(intent);
    }
}
