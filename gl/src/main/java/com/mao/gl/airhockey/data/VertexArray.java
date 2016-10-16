package com.mao.gl.airhockey.data;

import android.opengl.GLES20;

import com.mao.gl.airhockey.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Mao on 2016/10/16.
 */

public class VertexArray {

    private final FloatBuffer mFloatBuffer;

    public VertexArray(float[] vertexData) {
        //创建顶点缓冲区
        mFloatBuffer = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount,
                                       int stride) {
        mFloatBuffer.position(dataOffset);
        //把着色器属性与定点数据关联起来
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false,
                stride, mFloatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        mFloatBuffer.position(0);
    }
}
