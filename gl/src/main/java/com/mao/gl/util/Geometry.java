package com.mao.gl.util;

/**
 * Created by Mao on 2016/11/2.
 */

public class Geometry {

    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
    }

    public static class Circle {
        public Point center;
        public float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public Point center;
        public float radius;
        public float height;

        public Cylinder(Point center, float height, float radius) {
            this.center = center;
            this.height = height;
            this.radius = radius;
        }
    }
}
