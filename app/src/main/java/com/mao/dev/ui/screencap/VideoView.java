package com.mao.dev.ui.screencap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by Mao.
 */
public class VideoView extends TextureView implements TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener {
    private VideoViewListener mListener;
    protected MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private Context context;
    private ImageView imageView;
    private Matrix mMatrix;

    public VideoView(Context context) {
        this(context, null);
        this.context = context;
        setSurfaceTextureListener(this);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        setSurfaceTextureListener(this);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        mSurface = new Surface(surfaceTexture);
        if (mListener != null) {
            mListener.onSurfaceAvailable();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(mSurface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mListener != null) {
            mListener.onSurfaceDestoryed();
        }
        if (null != mSurface) {
            mSurface.release();
            mSurface = null;
        }
        surface.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mListener != null) {
            mListener.onMediaError(what);
        }
        return false;
    }

    public void layoutDisplay(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return;
        }
        float scaleX = getWidth() / (float) videoWidth;
        float scaleY = getHeight() / (float) videoHeight;

        float scale = Math.max(scaleX, scaleY);
        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
        mMatrix.reset();

        //移动视频
        mMatrix.preTranslate((getWidth() - videoWidth) / 2, (getHeight() - videoHeight) / 2);
        //缩放
        mMatrix.preScale(videoWidth / (float) getWidth(), videoHeight / (float) getHeight());
        Log.d("mao", "scale=" + scale);
        mMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
        setTransform(mMatrix);
        postInvalidate();
    }

    public void setDataSource(String path) throws IOException, IllegalStateException {
        initPlayer();
        mMediaPlayer.setDataSource(path);
    }

    public void setVideoURI(Uri uri) {
        initPlayer();
        try {
            mMediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageViewSrc(Uri uri, ImageView imageView){
        this.imageView = imageView;
        new ImageViewTask().execute(uri);
    }



    private void initPlayer() {
        setSurfaceTextureListener(this);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
//            mMediaPlayer.reset();
        }
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setOnPreparedListener(listener);
            try {
                mMediaPlayer.prepareAsync();
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void prepareAsync(MediaPlayer.OnPreparedListener listener)
            throws IllegalStateException {
        if (null != mMediaPlayer) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.prepareAsync();
        }
    }

    public void start() {
        if (null != mMediaPlayer) {
            try {
                if (null != mSurface) {
                    mMediaPlayer.setSurface(mSurface);
                }
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
            }
        }
    }

    public void startMute() {
        if (null != mMediaPlayer) {
            try {
                if (null != mSurface) {
                    mMediaPlayer.setSurface(mSurface);
                }
                mMediaPlayer.setVolume(0,0);
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
            }
        }
    }

    public void resume(){

        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        } catch (IllegalStateException e) {
        }
    }

    public void pause() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
        } catch (IllegalStateException e) {
        }
    }

    public void stop() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
        } catch (IllegalStateException e) {
        }
    }

    public int getDuration() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        }
        try {
            return mMediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
        }
        return false;
    }

    public int getCurrentPosition() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void seekTo(int msec) {
        if (isPlaying()) {
            mMediaPlayer.seekTo(msec);
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public void setLooping(boolean looping) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setLooping(looping);
        }
    }

    public void reset() {
        if (mMediaPlayer != null) {
            if (isPlaying()) {
                stop();
            }
            try {
                mMediaPlayer.reset();
            } catch (IllegalStateException e) {
            }
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            try {
                if (isPlaying()) {
                    stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
            }
        }
    }

    public void setVideoListener(VideoViewListener listener) {
        this.mListener = listener;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setOnCompletionListener(listener);
        }
    }

    public interface VideoViewListener {
        /**
         * surface可用时
         */
        void onSurfaceAvailable();

        /**
         * surface销毁时
         */
        void onSurfaceDestoryed();

        /**
         * mediaPlayer播放异常时调用
         *
         * @param what
         */
        void onMediaError(int what);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    class ImageViewTask extends AsyncTask<Uri,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(Uri... voids) {

            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(context,voids[0]);
            Bitmap bitmap = media.getFrameAtTime();
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}
