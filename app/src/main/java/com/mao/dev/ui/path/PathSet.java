package com.mao.dev.ui.path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 1/8/18.
 */
public class PathSet {

    private List<PathPoint> pathPoints = new ArrayList<>();

    public List<PathPoint> getPathPoints() {
        return pathPoints;
    }

    public void moveTo(float x, float y) {
        pathPoints.add(PathPointFactory.movePoint(x, y));
    }

    public void lineTo(float x, float y) {
        pathPoints.add(PathPointFactory.linePoint(x, y));
    }

    public void quadBezierTo(float c0x, float c0y, float x, float y) {
        pathPoints.add(PathPointFactory.lineQuadPoint(c0x, c0y, x, y));
    }

    public void cubicBezierTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        pathPoints.add(PathPointFactory.lineCubicPoint(c0x, c0y, c1x, c1y, x, y));
    }
}
