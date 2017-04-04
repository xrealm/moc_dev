package com.mao.hockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.mao.hockey.model.Geometry;
import com.mao.hockey.model.Mallet;
import com.mao.hockey.model.Puck;
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
    private float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float[] modelViewProjectionMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];

    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float nearBound = -0.8f;
    private final float farBound = 0.8f;

    private Table table;
    private Mallet mallet;
    private Puck puck;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;

    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;

    public HockeyRender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.6f, 0.6f, 0.6f, 0.0f);
        table = new Table();
        //半径和高度设置为一个大小，都是32个点
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);
        textureProgram = new TextureShaderProgram(mContext);
        colorProgram = new ColorShaderProgram(mContext);
        texture = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface);
        blueMalletPosition = new Geometry.Point(0, mallet.height / 2f, 0.4f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / height, 1f, 10.0f);
//        //设置为单位矩阵，再平移-2
//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 0, 0, -2.5f);
//        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0, 0);
//        float[] temp = new float[16];
//        //相乘
//        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        //创建一个反转矩阵
        Matrix.invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);

        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();

        //draw the mallet
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniform(modelViewProjectionMatrix, 1f, 0, 0);
        mallet.bindData(colorProgram);
        mallet.draw();

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z);
        colorProgram.setUniform(modelViewProjectionMatrix, 0, 0, 1f);
        mallet.draw();

        //draw the puck
        positionObjectInScene(0f, puck.height / 2f, 0);
        colorProgram.setUniform(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorProgram);
        puck.draw();
    }

    private void positionObjectInScene(float x, float y, float z) {
        //物体已经被定义好，只需要旋转
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionTableInScene() {
        //设置单位矩阵
        Matrix.setIdentityM(modelMatrix, 0);
        //桌子向后旋转90
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0, 0);
        //viewProjectionMatrix 和modelMatrix相乘存储到modelViewProjectionMatrix
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    public void handleTouchPress(float nomalizedX, float nomalizedY) {
        //触控点投射到射线上
        Geometry.Ray ray = covertNomalized2DPointToRay(nomalizedX, nomalizedY);
        //封装木槌
        Geometry.Sphere malletBoundSphere = new Geometry.Sphere(new Geometry.Point(
                blueMalletPosition.x,
                blueMalletPosition.y,
                blueMalletPosition.z
        ), mallet.height / 2f);
        //检测相交
        malletPressed = Geometry.intersects(malletBoundSphere, ray);
    }

    private Geometry.Ray covertNomalized2DPointToRay(float nomalizedX, float nomalizedY) {
        // z分量 -1到1,w分量先设置1
        float[] nearPointNdc = {nomalizedX, nomalizedY, -1, 1};
        float[] farPointNdc = {nomalizedX, nomalizedY, 1, 1};
        float[] nearPointWorld = new float[4];
        float[] farPointWorld = new float[4];
        //与invertedViewProjectionMatrix得到世界坐标, 有反转的w的值
        Matrix.multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        Matrix.multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);
        // 消除透视除法w分量的影响
        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPointRay = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay = new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));
    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    public void handleTouchDrag(float nomalizedX, float nomalizedY) {
        Geometry.Ray ray = covertNomalized2DPointToRay(nomalizedX, nomalizedY);
        Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0, 0, 0), new Geometry.Vector(0, 1, 0));
        Geometry.Point touchedPoint = Geometry.intersectionPoint(ray, plane);
        blueMalletPosition = new Geometry.Point(touchedPoint.x, mallet.height / 2f, touchedPoint.z);
//        blueMalletPosition = new Geometry.Point(
//                clamp(touchedPoint.x, leftBound + mallet.radius, rightBound - mallet.radius),
//                mallet.height / 2f,
//                clamp(touchedPoint.z, 0 + mallet.radius, nearBound - mallet.radius)
//        );
    }

    private float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(value, min));
    }

    public void handleTouchUp(float nomalizedX, float nomalizedY) {

    }
}
