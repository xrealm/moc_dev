package com.mao.gl.airhockey.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.mao.gl.R;
import com.mao.gl.airhockey.model.Mallet;
import com.mao.gl.airhockey.model.Puck;
import com.mao.gl.airhockey.model.Table;
import com.mao.gl.airhockey.program.ColorShaderProgram;
import com.mao.gl.airhockey.program.TextureShaderProgram;
import com.mao.gl.airhockey.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.mao.gl.airhockey.Constants.BYTES_PER_FLOAT;

/**
 * Created by Mao on 16/10/10.
 */

public class AirHockeyRender implements GLSurfaceView.Renderer {

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";
    // 存储矩阵
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;

    private TextureShaderProgram mTextureProgram;
    private ColorShaderProgram mColorProgram;
    private int mTexture;
    private Context mContext;

    // varying 混合
    private static final String VERTEX_SHADER =
            "uniform mat4 " + U_MATRIX + ";\n"
                    + "attribute vec4 a_Position;\n"
                    + "void main() {\n"
                    + "gl_Position = u_Matrix * a_Position;\n"
                    + "}";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
                    + "uniform vec4 u_Color;\n"
                    + "void main() {\n"
                    + "gl_FragColor = u_Color;\n"
                    + "}";

    private static final String TEXTURE_VERTEX_SHADER =
            "uniform mat4 u_Matrix;\n"
                    + "attribute vec4 a_Position;\n"
                    + "attribute vec2 a_TextureCoordinates;\n"
                    + "varying vec2 v_TextureCoordinates;\n"
                    + "void main() {\n"
                    + "v_TextureCoordinates = a_TextureCoordinates;\n"
                    + "gl_Position = u_Matrix * a_Position;\n"
                    + "}";

    private static final String TEXTURE_FRAGMENT_SHADER =
            "precision mediump float;\n"
                    +"uniform sampler2D u_TextureUnit;\n" //二维纹理数据数组
                    +"varying vec2 v_TextureCoordinates;\n"
                    +"void main() {\n"
                    +"gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);\n"
                    + "}";

    public AirHockeyRender(Context context) {
        mContext = context;
    }

    private String getVertexShader() {
        return VERTEX_SHADER;
    }

    private String getFragmentShader() {
        return FRAGMENT_SHADER;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
        mTable = new Table();
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);
        mTextureProgram = new TextureShaderProgram(TEXTURE_VERTEX_SHADER, TEXTURE_FRAGMENT_SHADER);
        mColorProgram = new ColorShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        mTexture = TextureHelper.loadTexture(mContext, R.mipmap.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //指定视图尺寸
        GLES20.glViewport(0, 0, width, height);

        //创建正交投影矩阵
//        final float aspectRatio = width > height ? (float) width / (float) height : (float) height / (float) width;
//        if (width > height) {
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }

        //使用投影矩阵
        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);
        //设置视口，眼睛的位置
        Matrix.setLookAtM(viewMatrix, 0, 0, 1.2f, 2.2f, 0, 0, 0, 0, 1f, 0);
//        //模型矩阵设为单位矩阵
//        Matrix.setIdentityM(modelMatrix, 0);
//        // 沿z轴平移 -2
//        // z平移
//        Matrix.translateM(modelMatrix, 0, 0, 0, -2.5f);
//        // 旋转-60
//        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0, 0);
//        // 投影矩阵和模型矩阵相乘
//        float[] temp = new float[16];
//        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        //结果放回投影矩阵
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清空屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        positionTableInScene();
        mTextureProgram.useProgram();
        mTextureProgram.setUniforms(modelViewProjectionMatrix, mTexture);
        mTable.bindData(mTextureProgram);
        mTable.draw();

        positionObjectInScene(0f, mMallet.height / 2f, -0.4f);
        mColorProgram.useProgram();
        mColorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0, 0);
        mMallet.bindData(mColorProgram);
        mMallet.draw();

        positionObjectInScene(0f, mMallet.height / 2f, 0.4f);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0, 0, 1f);
        mMallet.draw();

        //puck
        positionObjectInScene(0f, mMallet.height / 2f, 0);
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorProgram);
        mPuck.draw();
    }

    //更新table模型矩阵
    private void positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }
}
