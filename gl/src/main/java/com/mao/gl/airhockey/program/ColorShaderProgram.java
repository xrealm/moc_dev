package com.mao.gl.airhockey.program;

import android.opengl.GLES20;

/**
 * Created by Mao on 2016/10/23.
 */

public class ColorShaderProgram extends ShaderProgram {

    //uniform
    private int uMatrixLocation;
    private int uColorLocation;

    //attr
    private int aPositionLocation;

    public ColorShaderProgram(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        uColorLocation = GLES20.glGetUniformLocation(mProgram, U_COLOR);
    }

    public void setUniforms(float[] matrix, float r, float g, float b) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getColorAttributeLocation() {
        return 0;
    }
}
