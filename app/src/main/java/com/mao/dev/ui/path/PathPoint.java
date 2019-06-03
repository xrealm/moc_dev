package com.mao.dev.ui.path;

/**
 * Created by Mao on 1/8/18.
 */
public class PathPoint {

    /**
     * 起始点
     */
    public static final int MOVE = 0;
    /**
     * 直线路径
     */
    public static final int LINE = 1;
    /**
     * 二阶贝塞尔曲线
     */
    public static final int QUAD_CURVE = 2;
    /**
     * 三阶贝塞尔曲线
     */
    public static final int CUBIC_CURVE = 3;
    /**
     * 最终移动到的位置
     */
    public float x, y;
    /**
     * 控制点
     */
    public float c0x, c0y;
    public float c1x, c1y;
    //操作符
    public int operation;

    /**
     * Move/Line
     */
    PathPoint(int operation, float x, float y) {
        this.x = x;
        this.y = y;
        this.operation = operation;
    }

    /**
     * 二阶贝塞尔曲线
     *
     * @param c0x 第一个控制点x
     * @param c0y 第一个控制点y
     * @param x 目标点x
     * @param y 目标点y
     */
    PathPoint(float c0x, float c0y, float x, float y) {
        this.x = x;
        this.y = y;
        this.c0x = c0x;
        this.c0y = c0y;
        this.operation = QUAD_CURVE;
    }

    /**
     * 三阶贝塞尔曲线
     *
     * @param c0x 第一个控制点x
     * @param c0y 第一个控制点y
     * @param c1x 第二个控制点x
     * @param c1y 第二个控制点y
     * @param x 目标点x
     * @param y 目标点y
     */
    PathPoint(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        this.x = x;
        this.y = y;
        this.c0x = c0x;
        this.c0y = c0y;
        this.c1x = c1x;
        this.c1y = c1y;
        this.operation = CUBIC_CURVE;
    }

    @Override
    public String toString() {
        return "PathPoint{" +
                "x=" + x +
                ", y=" + y +
                ", c0x=" + c0x +
                ", c0y=" + c0y +
                ", c1x=" + c1x +
                ", c1y=" + c1y +
                ", operation=" + operation +
                '}';
    }
}
