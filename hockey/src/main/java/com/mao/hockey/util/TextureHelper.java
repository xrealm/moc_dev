package com.mao.hockey.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.orhanobut.logger.Logger;

/**
 * Created by Mao on 2017/3/31.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resId) {
        int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);//创建纹理对象
        if (textureObjectId[0] == 0) {
            Logger.w("Could not generate a new OpenGL texture object.");
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        //绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId[0]);
        //缩小情况三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //放大情况双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //生成级别
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectId[0];
    }
}
