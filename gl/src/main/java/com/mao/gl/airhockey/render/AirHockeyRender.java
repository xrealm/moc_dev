package com.mao.gl.airhockey.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.mao.gl.airhockey.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 16/10/10.
 */

public class AirHockeyRender implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";

    private static final String VERTEX_SHADER =
            "attribute vec4 a_Position;\n"
                    + "void main() {\n"
                    + "gl_Position = a_Position;\n"
//                    + "gl_PointSize=10.0\n"
                    + "}";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
                    + "uniform vec4 u_Color;\n"
                    + "void main() {\n"
                    + "gl_FragColor = u_Color;\n"
                    + "}";

    private final FloatBuffer mVertexData;
    private int program;
    private int uColorLocation;
    private int aPositionLocation;

    public AirHockeyRender() {
        //逆时针排列三角形顶点,卷曲顺序
        float[] tableVertices = {
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,

                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,

                //line1
                -0.5f, 0,
                0.5f, 0,

                //mallets
                0, -0.25f,
                0, 0.25f
        };

        //分配本地内存
        mVertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexData.put(tableVertices);
    }

    private String getVertexShader() {
        return VERTEX_SHADER;
    }

    private String getFragmentShader() {
        return FRAGMENT_SHADER;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //
        GLES20.glClearColor(1, 1, 0, 0);
        int vertexShader = ShaderHelper.compileVertexShader(VERTEX_SHADER);
        int fragmentShader = ShaderHelper.complieFragmentShader(FRAGMENT_SHADER);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        ShaderHelper.validateProgram(program);
        GLES20.glUseProgram(program);

        // 获取uniform位置,存入
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        //获取属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        //移动指针到开头处
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //指定视图尺寸
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // 更新u_Color值
        GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        //绘制分隔线
        GLES20.glUniform4f(uColorLocation, 1f, 0, 0, 1f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制木槌
        GLES20.glUniform4f(uColorLocation, 0, 0, 1f, 1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glUniform4f(uColorLocation, 1, 0, 0, 1);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

        //绘制
    }
}
