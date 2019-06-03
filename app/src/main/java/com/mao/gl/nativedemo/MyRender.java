package com.mao.gl.nativedemo;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/10/23.
 */

public class MyRender implements GLSurfaceView.Renderer {


    public MyRender(Context context) {

    }

    private String getFragmentShader() {

        return null;
    }

    private String getVertexShader() {

        return null;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = getVertexShader();
        String fragmentShader = getFragmentShader();
        nativeInit(vertexShader, fragmentShader);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        nativeSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        natvieDraw(60, 60);
    }

    private static native void nativeInit(String vertexShader, String fragmentShader);
    private static native void nativeSurfaceChanged(int width, int height);

    private static native void natvieDraw(float angleX, float angleY);
}
