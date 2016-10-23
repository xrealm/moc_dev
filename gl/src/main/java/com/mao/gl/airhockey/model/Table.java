package com.mao.gl.airhockey.model;

import com.mao.gl.airhockey.Constants;
import com.mao.gl.airhockey.data.VertexArray;
import com.mao.gl.airhockey.program.TextureShaderProgram;

/**
 * Created by Mao on 2016/10/17.
 */

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)
            * Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // order of coordinates x, y, s, t
            0,     0,     0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
    };
    private final VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {

    }
}
