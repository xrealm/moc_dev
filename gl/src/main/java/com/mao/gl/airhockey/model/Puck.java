package com.mao.gl.airhockey.model;

import com.mao.gl.airhockey.data.VertexArray;
import com.mao.gl.util.Geometry;

import java.util.List;

/**
 * Created by Mao on 2016/11/5.
 */

public class Puck {

    public static final int POSITION_COMPONENT_COUNT = 3;

    public float radius, height;
    private VertexArray mVertexArray;
    private List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GenerateData puck = ObjectBuilder.createPuck(new Geometry.Cylinder(new Geometry.Point(0, 0, 0), radius, height), numPointsAroundPuck);
        mVertexArray = new VertexArray(puck.vertexData);
        drawList = puck.drawList;
    }
}
