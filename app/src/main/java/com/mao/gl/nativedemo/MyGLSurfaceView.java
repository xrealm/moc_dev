package com.mao.gl.nativedemo;

import android.content.Context;
import android.content.res.ObbInfo;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;

/**
 * Created by Mao on 2017/10/23.
 */

public class MyGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Render mRender;
    private final WeakReference<MyGLSurfaceView> mSurfaceViewWeakRef = new WeakReference<MyGLSurfaceView>(this);
    private RenderThread mRenderThread;

    public MyGLSurfaceView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    public void setRender(Render render) {
        mRender = render;
        mRenderThread = new RenderThread(mSurfaceViewWeakRef);
        mRenderThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRenderThread.surfaceCreated();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mRenderThread.surfaceChanged(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRenderThread.surfaceDestoryed();
    }

    public interface Render {
        void onSurfaceCreated();

        void onSurfaceChanged();

        void onDrawFrame();
    }

    class RenderThread extends Thread {

        private Object lock = new Object();
        private boolean mHasSurface;
        public int mWidth;
        public int mHeight;
        private WeakReference<MyGLSurfaceView> mGlSurfaceViewRef;
        private EglHelper mEglHelper;


        public RenderThread(WeakReference<MyGLSurfaceView> surfaceViewWeakRef) {
            mGlSurfaceViewRef = surfaceViewWeakRef;
        }

        @Override
        public void run() {
            super.run();
            render();
        }

        private void render() {
            if (mEglHelper == null) {
                mEglHelper = new EglHelper(mGlSurfaceViewRef);
            }

            synchronized (lock) {
                if (!mHasSurface) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mEglHelper.start();
            MyGLSurfaceView glSurfaceView = mGlSurfaceViewRef.get();
            if (glSurfaceView != null) {
                glSurfaceView.mRender.onSurfaceCreated();
                glSurfaceView.mRender.onSurfaceChanged();
                glSurfaceView.mRender.onDrawFrame();
                mEglHelper.swapBuffer();
            }
        }

        public void surfaceCreated() {


        }

        public void surfaceChanged(int width, int height) {
            mWidth = width;
            mHeight = height;
            synchronized (lock) {
                mHasSurface = true;
                lock.notifyAll();
            }
        }

        public void surfaceDestoryed() {
            synchronized (lock) {
                mHasSurface = false;
                lock.notifyAll();
            }
        }
    }

    class EglHelper {
        private WeakReference<MyGLSurfaceView> mGlSurfaceViewRef;
        private EGL14 mEGL14;
        private EGLDisplay mEGLDisplay;
        private EGLConfig mEGLConfig;
        private EGLContext mEGLContext;
        private EGLSurface mEGLSurface;

        public EglHelper(WeakReference<MyGLSurfaceView> glSurfaceViewRef) {
            mGlSurfaceViewRef = glSurfaceViewRef;
        }

        public void start() {
            int[] numConfig = new int[1];
            EGLConfig[] eglConfigs = new EGLConfig[1];
            int[] configSpec = {
                    EGL14.EGL_RED_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_SURFACE_TYPE,
                    EGL14.EGL_WINDOW_BIT,
                    EGL14.EGL_NONE
            };
            mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            EGL14.eglChooseConfig(mEGLDisplay, configSpec, 0, eglConfigs, 0, 0, numConfig, 0);
            if (numConfig[0] <= 0) {
                return;
            }
            mEGLConfig = eglConfigs[0];
            int[] surfaceAttribs = {
                    EGL14.EGL_WIDTH, 1,
                    EGL14.EGL_HEIGHT, 1,
                    EGL14.EGL_NONE
            };

            int[] contextSpec = new int[] {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            mEGLContext = EGL14.eglCreateContext(mEGLDisplay, mEGLConfig, EGL14.EGL_NO_CONTEXT, contextSpec, 0);
            if (EGL14.EGL_NO_CONTEXT == mEGLContext) {
                return;
            }
            int[] value = new int[1];
            EGL14.eglQueryContext(mEGLDisplay, mEGLContext, 0, value, 0);
            mEGLSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, mEGLConfig, surfaceAttribs, 0);
            if (mEGLSurface == null || mEGLSurface == EGL14.EGL_NO_SURFACE) {
                return;
            }
            EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
        }

        public void swapBuffer() {
            if (mEGLDisplay != null && mEGLSurface != null) {
                boolean swap = EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);
                if (!swap) {
                    Log.e("mao", "eglSwapBuffer failed");
                }
            }
        }

        public void destorySurface() {
            if (mEGLSurface != null) {
                EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
                EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
                mEGLSurface = null;
            }
            if (mEGLContext != null) {
                EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
            }
            if (mEGLDisplay != null) {
                EGL14.eglTerminate(mEGLDisplay);
                mEGLDisplay = null;
            }
        }
    }
}
