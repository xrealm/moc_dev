package com.mao.gl.airhockey.program;

import android.opengl.GLES20;

/**
 * Created by mpg on 2016/10/19 .
 */

public class TextureShaderProgram extends ShaderProgram {
    // uniform
    private int uMatrixLocation;
    private int uTextureUnitLocation;
    //attr
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public TextureShaderProgram(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
    }

    /**
     * 设置uniform,传递矩阵和纹理给他们的uniform
     * @param matrix
     * @param textureId
     */
    public void setUniforms(float[] matrix, int textureId) {
        //传递矩阵给他的uniform
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //把活动的纹理单元设置为0
        GLES20.glActiveTexture(textureId);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //把选定的纹理单元传递给片段着色器中的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}
