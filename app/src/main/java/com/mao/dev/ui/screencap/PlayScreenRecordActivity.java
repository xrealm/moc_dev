package com.mao.dev.ui.screencap;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mao.dev.AppKit;
import com.mao.dev.R;

import java.io.File;

/**
 * Created by Mao on 2016/12/7.
 */

public class PlayScreenRecordActivity extends AppCompatActivity {

    public static final String KEY_VIDEO_PATH = "key_video_path";
    public static final String KEY_VIDEO_THUMB_PATH = "key_video_thumb_path";

    private ImageView mVideoThumb;
    private FullVideoView mVideoView;
    private TextView mBtnShare;
    private View mUploadContainer;

    private String mVideoPath, mThumbPath;
    private int mLastPostion;
    private boolean isStoped;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hani_activity_play_screen_record);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mVideoView = (FullVideoView) findViewById(R.id.vv_play_record);
        mVideoThumb = (ImageView) findViewById(R.id.iv_screen_record_thumb);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mLastPostion > 0) {
                mVideoView.seekTo(mLastPostion);
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        mVideoView.start();
                    }
                });
            }
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
//            mVideoView.adjustSize(videoWidth, videoHeight);
//            layoutDisplay(videoWidth, videoHeight);
            mVideoView.layoutDisplay(videoWidth, videoHeight, AppKit.getScreenWidth(), AppKit.getScreenHeight());
//            mVideoView.layoutDisplay(videoWidth, videoHeight);
        }
    };

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

        if (!TextUtils.isEmpty(mVideoPath)) {
            mVideoView.setVideoURI(Uri.parse(mVideoPath));
        }
    }

    private void initEvent() {
        mVideoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoThumb.setVisibility(View.GONE);
                play(mVideoPath);
            }
        });
    }

    private void play(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (mVideoView == null || !file.exists() || mVideoView.isPlaying()) {
            return;
        }
        try {
//            if (isStoped) {
//                mVideoView.setVideoURI(Uri.parse(mVideoPath));
//            }
            mVideoView.setOnPreparedListener(mPreparedListener);
            mVideoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mLastPostion = mVideoView.getCurrentPosition();
            isStoped = true;
            mVideoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        play(mVideoPath);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            try {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoThumb.setVisibility(View.GONE);
                    mVideoView.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
