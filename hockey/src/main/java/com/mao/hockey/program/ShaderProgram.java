package com.mao.hockey.program;

import android.content.Context;
import android.opengl.GLES20;

import com.mao.hockey.util.OpenGLUtil;
import com.mao.hockey.util.ShaderHelper;

/**
 * Created by Mao on 2017/3/31.
 */

public class ShaderProgram {
    //uniform constant
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTUREUNIT = "u_TextureUnit";
    //attribute constant
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURECOORDINATES = "a_TextureCoordinates";

    //shader program
    protected final int program;

    public ShaderProgram(Context context, int vertexShaderResId, int fragmentShaderResId) {
        program = ShaderHelper.buildProgram(
                OpenGLUtil.readShaderFromRaw(context, vertexShaderResId),
                OpenGLUtil.readShaderFromRaw(context, fragmentShaderResId));
    }

    public void useProgram() {
        //set the current opengl shader program to this program
        GLES20.glUseProgram(program);
    }
}
