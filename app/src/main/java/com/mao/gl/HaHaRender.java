package com.mao.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.mao.dev.R;
import com.mao.dev.ui.blur.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/5/10.
 */

public class HaHaRender implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;

    private int mProgram;
    private int mPositionLocation;
    private int mColorLocation;
    private Context mContext;

    float[] vCoords = {
            0f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0
    };

    float[] vColor = {1.0f, 1.0f, 1.0f, 1.0f};

    public HaHaRender(Context context) {
        mContext = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(vCoords.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vCoords);
        floatBuffer.position(0);

        mProgram = ShaderHelper.buildProgram(
                OpenGLUtil.readShaderFromRawResouce(mContext, R.raw.vertex_shader),
                OpenGLUtil.readShaderFromRawResouce(mContext, R.raw.fragment_shader));

        mPositionLocation = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionLocation);
        GLES20.glVertexAttribPointer(mPositionLocation, 3, GLES20.GL_FLOAT, false, 0, floatBuffer);
        mColorLocation = GLES20.glGetUniformLocation(mProgram, "vColor");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniform4fv(mColorLocation, 1, vColor, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 9);
    }
}
