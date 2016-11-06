package com.mao.gl.airhockey.model;

import android.opengl.GLES20;

import com.mao.gl.airhockey.Constants;
import com.mao.gl.airhockey.data.VertexArray;
import com.mao.gl.airhockey.program.ColorShaderProgram;
import com.mao.gl.util.Geometry;

import java.util.List;

/**
 * Created by Mao on 2016/10/17.
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public float radius;
    public float height;
    private List<ObjectBuilder.DrawCommand> drawList;
    private VertexArray mVertexArray;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GenerateData mallet = ObjectBuilder.createMallet(new Geometry.Point(0, 0, 0), radius, height, numPointsAroundMallet);
        mVertexArray = new VertexArray(mallet.vertexData);
        drawList = mallet.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        mVertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
