package com.mao.hockey.model;

import com.mao.hockey.VertexArray;
import com.mao.hockey.program.ColorShaderProgram;

import java.util.List;

/**
 * Created by Mao on 2017/4/2.
 */

public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createPuck(new Gemetry.Cylinder(
                new Gemetry.Point(0, 0, 0), radius, height), numPointsAroundPuck);
        vertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
