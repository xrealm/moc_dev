package com.mao.hockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.mao.hockey.model.Mallet;
import com.mao.hockey.model.Table;
import com.mao.hockey.program.ColorShaderProgram;
import com.mao.hockey.program.TextureShaderProgram;
import com.mao.hockey.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/3/19.
 */

public class HockeyRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];//模型矩阵，把桌子平移出来

    private Table table;
    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    public HockeyRender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.6f, 0.6f, 0.6f, 0.0f);
        table = new Table();
        mallet = new Mallet();
        textureProgram = new TextureShaderProgram(mContext);
        colorProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface);
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

        //draw table
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        //mallet
        colorProgram.useProgram();
        colorProgram.setUniform(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }
}
