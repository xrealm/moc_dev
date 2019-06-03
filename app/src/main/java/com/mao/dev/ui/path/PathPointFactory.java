package com.mao.dev.ui.path;

/**
 * Created by Mao on 2/8/18.
 */
public class PathPointFactory {

    public static PathPoint movePoint(float x, float y) {
        PathPoint pathPoint = new PathPoint(PathPoint.MOVE, x, y);
        pathPoint.operation = PathPoint.MOVE;
        return pathPoint;
    }

    public static PathPoint linePoint(float x, float y) {
        PathPoint pathPoint = new PathPoint(PathPoint.MOVE, x, y);
        pathPoint.operation = PathPoint.LINE;
        return pathPoint;
    }

    public static PathPoint lineQuadPoint(float c0x, float c0y, float x, float y) {
        PathPoint pathPoint = new PathPoint(c0x, c0y, x, y);
        pathPoint.operation = PathPoint.QUAD_CURVE;
        return pathPoint;
    }

    public static PathPoint lineCubicPoint(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        PathPoint pathPoint = new PathPoint(c0x, c0y, c1x, c1y, x, y);
        pathPoint.operation = PathPoint.CUBIC_CURVE;
        return pathPoint;
    }
}
