package com.mao.gl.airhockey.program;

import android.opengl.GLES20;

/**
 * Created by Mao on 2016/10/23.
 */

public class ColorShaderProgram extends ShaderProgram {

    //uniform
    private int uMatrixLocation;

    //attr
    private int aPositionLocation;
    private int aColorLocation;

    public ColorShaderProgram(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return aColorLocation;
    }
}
