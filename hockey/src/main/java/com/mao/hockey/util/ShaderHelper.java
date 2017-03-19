package com.mao.hockey.util;

import android.opengl.GLES20;

import com.orhanobut.logger.Logger;

/**
 * Created by Mao on 2017/3/19.
 */

public class ShaderHelper {

    public static int compileVertexShader(String shader) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shader);
    }

    public static int compileFragmentShader(String shader) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shader);
    }

    private static int compileShader(int type, String shader) {
        //创建OpenGL对象的引用
        int shaderObjectId = GLES20.glCreateShader(type);
        if (shaderObjectId == 0) {
            Logger.w("Could not create new shader.");
        }
        //bind着色器对象
        GLES20.glShaderSource(shaderObjectId, shader);
        //编译shader代码
        GLES20.glCompileShader(shaderObjectId);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Logger.v("Result of compiling source: " + "\n" + shader + "\n"
                + GLES20.glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            // delete the shader object
            GLES20.glDeleteShader(shaderObjectId);
            Logger.w("Compilation of shader failed.");
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            Logger.w("Could not create new program.");
            return 0;
        }
        //附上着色器
        GLES20.glAttachShader(program, vertexShaderId);
        GLES20.glAttachShader(program, fragmentShaderId);
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        Logger.v("Result of linking program:\n" + GLES20.glGetProgramInfoLog(program));
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(program);
            Logger.w("Linking of program failed.");
            return 0;
        }
        return program;
    }

    public static boolean validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Logger.v("Results of validating program: " + validateStatus[0]
                + "\nLog:" + GLES20.glGetProgramInfoLog(programId));
        return validateStatus[0] != 0;
    }
}
