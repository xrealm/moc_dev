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
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    //数组跨距
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;

    // varying 混合
    private static final String VERTEX_SHADER =
            "attribute vec4 a_Position;\n"
                    + "attribute vec4 a_Color;\n"
                    + "varying vec4 v_Color;\n"
                    + "void main() {\n"
                    + "v_Color = a_Color;\n"
                    + "gl_Position = a_Position;\n"
                    + "gl_PointSize = 5.0;\n"
                    + "}";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
                    + "varying vec4 v_Color;\n"
                    + "void main() {\n"
                    + "gl_FragColor = v_Color;\n"
                    + "}";

    private final FloatBuffer mVertexData;
    private int program;
    private int aPositionLocation;

    public AirHockeyRender() {
        //逆时针排列三角形顶点,卷曲顺序
        float[] tableVertices = {
                // x,y,r,g,b
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

                //line1
                -0.5f, 0, 1f, 0f, 0f,
                0.5f, 0, 1f, 0f, 0f,

                //mallets
                0, -0.25f, 0f, 0f, 1f,
                0, 0.25f, 1f, 0f, 0f
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

        //获取属性位置
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        // varying a_Color
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        //移动指针到开头处
        mVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 调到第一个颜色属性
        mVertexData.position(POSITION_COMPONENT_COUNT);
        // 把颜色数据和shader中a_Color关联起来
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, mVertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
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
        //绘制三角形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        // 三角形扇
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //绘制分隔线
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //绘制木槌
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

        //绘制
    }
}
