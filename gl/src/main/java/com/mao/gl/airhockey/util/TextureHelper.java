package com.mao.gl.airhockey.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Mao on 2016/10/16.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resourceId) {
        final int[] textureId = new int[1];
        GLES20.glGenTextures(1, textureId, 0);
        if (textureId[0] == 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureId, 0);
            return 0;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        //设置纹理过滤参数
        // 缩小情况下，三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        // 放大情况下，双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载bitmap到OpenGL里
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //生成MIP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureId[0];
    }
}
