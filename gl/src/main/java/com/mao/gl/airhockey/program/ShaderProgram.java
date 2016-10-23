package com.mao.gl.airhockey.program;

import android.opengl.GLES20;

import com.mao.gl.airhockey.util.ShaderHelper;

/**
 * Created by mpg on 2016/10/19 .
 */

public class ShaderProgram {
    // uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    // attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int mProgram;

    protected ShaderProgram(String vertexShader, String fragmentShader) {
        mProgram = ShaderHelper.buildProgram(vertexShader, fragmentShader);
    }

    public void useProgram() {
        GLES20.glUseProgram(mProgram);
    }
}
