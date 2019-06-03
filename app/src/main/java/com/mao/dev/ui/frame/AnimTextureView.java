package com.mao.dev.ui.frame;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.TextureView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mao on 2017/10/20.
 */

public class AnimTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final int MSG_RENDER = 1;

    private Paint mPaint;
    private Rect mSrcRect;
    private Rect mDstRect;

    private int mCurrentFrame;
    private int mFrameNumber;
    private int[] mResourcePath;

    private Handler mHandler;
    private RenderThread mRenderThread;
    private int mGapTime = 50;

    private byte[] mLock = new byte[1];
    private boolean isRunning;

//    private List<Bitmap> mReusableBitmaps = Collections.synchronizedList(new ArrayList<Bitmap>());

    public AnimTextureView(Context context) {
        super(context);
        init();
    }

    public AnimTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOpaque(false);
        setSurfaceTextureListener(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mSrcRect = new Rect();
        mDstRect = new Rect();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        isRunning = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void start(int[] path, int frameRate) {
        if (mResourcePath == null) {
            mGapTime = Math.round(1000.0f / frameRate);
            mFrameNumber = path.length;
            mResourcePath = path;

            if (!isRunning) {
                schedule();
            }
        }
    }

    private void schedule() {
        mRenderThread = new RenderThread("AnimTextureView");
        mRenderThread.start();
        isRunning = true;
        mHandler = new Handler(mRenderThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (isRunning) {
                    onDrawFrame();
                    long nexttime = SystemClock.uptimeMillis() + mGapTime;
                    mHandler.sendEmptyMessageAtTime(MSG_RENDER, nexttime);
                }
            }
        };
        mHandler.sendEmptyMessage(MSG_RENDER);
    }

    public void stop() {
        quit();
        isRunning = false;
    }

    public void quit() {
        if (mRenderThread != null) {
            mRenderThread.quit();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    class RenderThread extends HandlerThread {

        public RenderThread(String name) {
            super(name);
        }
    }

    private void onDrawFrame() {
        try {
            Bitmap bitmap = loadBitmap(mResourcePath[mCurrentFrame]);
            drawBitmap(bitmap);
            recycleBitmap(bitmap);
//            addReusable(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCurrentFrame >= mResourcePath.length - 1) {
                mCurrentFrame = 0;
            } else {
                mCurrentFrame++;
            }
        }
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

//    private Bitmap getBitmapFromReusable() {
//        if (mReusableBitmaps.isEmpty()) {
//            return null;
//        }
//        return mReusableBitmaps.get(0);
//    }
//
//    private void addReusable(Bitmap bitmap) {
//        if (mReusableBitmaps.size() >= 1) {
//            recycleBitmap(mReusableBitmaps.remove(0));
//        }
//        mReusableBitmaps.add(bitmap);
//    }


    private Bitmap loadBitmap(int path) throws IOException {
//        return BitmapFactory.decodeStream(getResources().getAssets().open(path));
        InputStream inputStream = getResources().openRawResource(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        inputStream.mark(inputStream.available());
        BitmapFactory.decodeResource(getResources(), path, options);
        options.inJustDecodeBounds = false;
        inputStream.reset();

//        Bitmap inBitmap = getBitmapFromReusable();
//        options.inMutable = true;
//        if (inBitmap != null) {
//            options.inBitmap = inBitmap;
//        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();
        return bitmap;
    }

    private void drawBitmap(Bitmap bitmap) {
        Canvas canvas = lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDstRect.set(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(bitmap, mSrcRect, mDstRect, mPaint);
        unlockCanvasAndPost(canvas);
    }

}
