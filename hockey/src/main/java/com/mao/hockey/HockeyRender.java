package com.mao.hockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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

    private static final String U_MATRIX = "u_Matrix";
    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPOMENT_COUNT = 3;
    private static final String A_POSITION = "a_Position";
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPOMENT_COUNT = 4;
    private static final int STRIDE = (POSITION_COMPOMENT_COUNT + COLOR_COMPOMENT_COUNT) * BYTES_PER_FLOAT;
    private FloatBuffer vertexData;
    private Context mContext;
    private int program;
    private int aPositionLocation;
    private int aColorLocation;
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];//模型矩阵，把桌子平移出来
    private int uMatrixLocation;

    public HockeyRender(Context context) {
        mContext = context;
        float[] tableVerticesWithTriangles = {
                // order of coordinates: x,y,w,r,g,b
                // triangle fan
                0, 0, 0, 1.5f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0, 1f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0, 1f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0, 2f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0, 2f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0, 1f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 0, 1.5f, 1f, 0f, 0f,
                0.5f, 0f, 0, 1.5f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0, 1.25f, 0f, 0f, 1f,
                0f, 0.4f, 0, 1.75f, 1f, 0f, 0f,
        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 0, 0, 0.0f);
        String vertexShaderSource = OpenGLUtil.readShaderFromRaw(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = OpenGLUtil.readShaderFromRaw(mContext, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        ShaderHelper.validateProgram(program);
        GLES20.glUseProgram(program);
        //获取uniform的位置，用于给u_Color赋值
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        //获取attribute位置，给a_Position赋值
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        // matrix
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        //保证从0开始读取数据
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPOMENT_COUNT,
                GLES20.GL_FLOAT, false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        //从第一个颜色属性开始读
        vertexData.position(POSITION_COMPOMENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPOMENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / height, 1f, 10.0f);
        //设置为单位矩阵，再平移-2
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0, 0, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0, 0);
        float[] temp = new float[16];
        //相乘
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        //更新u_Color的值
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //line
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        //mallet
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}
