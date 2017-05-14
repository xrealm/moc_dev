package com.mao.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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
    private int vMatrixLocation;
    private int aColorLocation;
    private Context mContext;

    private float[] mProjectMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMvpMatrix = new float[16];

    float[] vCoords = {
            0f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0
    };

    float[] vColor = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f
    };

    public HaHaRender(Context context) {
        mContext = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        float[] circleVertex = createPosition(6, 0.5f);
        FloatBuffer vertexData = ByteBuffer.allocateDirect(circleVertex.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(circleVertex);
        vertexData.position(0);

        FloatBuffer colorData = ByteBuffer.allocateDirect(vColor.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vColor);
        colorData.position(0);

        mProgram = ShaderHelper.buildProgram(
                OpenGLUtil.readShaderFromRawResouce(mContext, R.raw.vertex_shader),
                OpenGLUtil.readShaderFromRawResouce(mContext, R.raw.fragment_shader));

        mPositionLocation = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionLocation);
        GLES20.glVertexAttribPointer(mPositionLocation, 3, GLES20.GL_FLOAT, false, 0, vertexData);
        mColorLocation = GLES20.glGetUniformLocation(mProgram, "vColor");
        vMatrixLocation = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        aColorLocation = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(aColorLocation);
        GLES20.glVertexAttribPointer(mColorLocation, 4, GLES20.GL_FLOAT, false, 0, colorData);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float radio = (float) width / height;
//        Matrix.perspectiveM(mProjectMatrix, 0, 0, radio, 3, 7);
        //透视投影
        Matrix.frustumM(mProjectMatrix, 0, -radio, radio, -1, 1, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(mMvpMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(vMatrixLocation, 1, false, mMvpMatrix, 0);
        GLES20.glUniform4fv(mColorLocation, 1, vColor, 0);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 9);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 8);
    }

    private float[] createPosition(int num, float radius) {
        int numVertices = 1 + (num + 1);
        float[] vertex = new float[numVertices * 3];
        int offset = 0;
        //center
        vertex[offset++] = 0;
        vertex[offset++] = 0;
        vertex[offset++] = 0;
        for (int i = 0; i <= num; i++) {
            float angle = (float) i / (float) num * ((float) Math.PI * 2.0f);
            vertex[offset++] = (float) (0 + radius * Math.cos(angle));
            vertex[offset++] = (float) (0 + radius * Math.sin(angle));
            vertex[offset++] = 0;
        }
        return vertex;
    }
}
