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

        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
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

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.height = height;
            this.radius = radius;
        }
    }

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * @return 定向向量长度
         */
        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        /**
         *
         * @return 两个向量交叉乘积
         */
        public Vector crossProduct(Vector other) {
            return new Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x));
        }

        /**
         * 计算两个向量之前的点积
         * @param other
         * @return
         */
        public float doProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        /**
         * 缩放向量
         * @param factor
         * @return
         */
        public Vector scale(float factor) {
            return new Vector(x * factor, y * factor, z * factor);
        }
    }

    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }
    }

    public static class Sphere {

        public final Point center;
        public final float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    /**
     * 平面定义 包含一个垂直于平面的法向向量和平面上的一个点
     */
    public static class Plane {
        public final Point point;
        public final Vector normal;

        public Plane(Point point, Vector normal) {
            this.point = point;
            this.normal = normal;
        }
    }

    /**
     *
     * @return 近点指向远点的向量
     */
    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    /**
     *
     * @return 射线与球是否相交
     */
    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * 用向量计算距离
     */
    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);

        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lenghOfBase = ray.vector.length();
        // 三角形的面积计算高度
        float distanceFromPointToRay = areaOfTriangleTimesTwo / lenghOfBase;
        return distanceFromPointToRay;
    }

    /**
     * 计算平面与射线的相交点
     * @return 射线与给定平面相交点
     */
    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
        float scaleFactor = rayToPlaneVector.doProduct(plane.normal)
                / ray.vector.doProduct(plane.normal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }
}
