package com.mao.hockey.model;

import com.mao.hockey.VertexArray;
import com.mao.hockey.program.ColorShaderProgram;

import java.util.List;

/**
 * Created by Mao on 2017/3/31.
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;
    private VertexArray mVertexArray;
    private List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroiundMallet) {
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createMallet(new Gemetry.Point(0, 0, 0),
                radius, height, numPointsAroiundMallet);
        this.radius = radius;
        this.height = height;
        mVertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        mVertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
