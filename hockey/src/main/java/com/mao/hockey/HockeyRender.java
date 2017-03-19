package com.mao.hockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.mao.hockey.util.OpenGLUtil;
import com.mao.hockey.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/3/19.
 */

public class HockeyRender implements GLSurfaceView.Renderer {

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPOMENT_COUNT = 2;
    private FloatBuffer vertexData;
    private Context mContext;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;

    public HockeyRender(Context context) {
        mContext = context;
        float[] tableVerticesWithTriangles = {
                // Triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f, 0.25f
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0.5f);
        String vertexShaderSource = OpenGLUtil.readShaderFromRaw(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = OpenGLUtil.readShaderFromRaw(mContext, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        ShaderHelper.validateProgram(program);
        GLES20.glUseProgram(program);
        //获取uniform的位置，用于给u_Color赋值
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        //获取attribute位置，给a_Position赋值
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        //保证从0开始读取数据
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPOMENT_COUNT,
                GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //更新u_Color的值
        GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        //line
        GLES20.glUniform4f(uColorLocation, 1, 0, 0, 1);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //mallet
        GLES20.glUniform4f(uColorLocation, 0, 0, 1, 1);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glUniform4f(uColorLocation, 1, 0, 0, 1);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}
