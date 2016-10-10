package com.mao.gl.airhockey.util;

import android.opengl.GLES20;
import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;

/**
 * Created by Mao on 16/10/10.
 */

public class ShaderHelper {

    private static final String TAG = ShaderHelper.class.getSimpleName();

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int complieFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            Log.w(TAG, "Could not create new shader.");
        }
        //上传着色器源码到着色器对象
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        //编译着色器
        GLES20.glCompileShader(shaderObjectId);
        //检查是否成功编译
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);
        Log.v(TAG, "Result of compiling source:\n" + shaderCode + "\n:" + GLES20.glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            Log.w(TAG, "Compilation of shader failed.");
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //新建程序对象
        int programId = GLES20.glCreateProgram();
        if (programId == 0) {

        }
        //着色器附加到程序
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        //链接起来
        GLES20.glLinkProgram(programId);
        //检查是否链接成功
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Log.v(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programId));
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            Log.w(TAG, "Linking of program failed.");
        }
        return programId;
    }

    /**
     * 验证OpenGL程序对象
     * @param programId 对象id
     * @return
     */
    public static boolean validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programId, GL_VALIDATE_STATUS, validateStatus, 0);
        return validateStatus[0] != 0;
    }
}
