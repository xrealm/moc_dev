package com.mao.hockey.program;

import android.content.Context;
import android.opengl.GLES20;

import com.mao.hockey.R;

/**
 * Created by Mao on 2017/3/31.
 */

public class TextureShaderProgram extends ShaderProgram {

    //uniform
    private int uMatrixLocation;
    private int uTextureUnit;

    //attribute
    private int aPositionLocation;
    private int aTextureCoordinates;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnit = GLES20.glGetUniformLocation(program, U_TEXTUREUNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURECOORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        //传递矩阵
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //活动纹理单元设置为单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureUnit, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinates;
    }
}
