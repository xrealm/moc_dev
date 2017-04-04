package com.mao.hockey.model;

/**
 * Created by Mao on 2017/4/2.
 */

public class Geometry {

    public static Vector vectorBetween(Point from, Point to) {
        return new Vector(to.x - from.x, to.y - from.y, to.z - from.z);
    }

    public static boolean intersects(Sphere sphere, Ray ray) {
        return distanceBetween(sphere.center, ray) < sphere.radius;
    }

    /**
     * 用向量计算距离
     *
     * @param point center
     * @param ray   射线
     * @return 距离
     */
    private static float distanceBetween(Point point, Ray ray) {
        Vector p1ToPoint = vectorBetween(ray.point, point);
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);
        //计算两个向量叉积，等于三角形面积2倍
        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lenghOfBase = ray.vector.length();

        float distanceFromPointToRay = areaOfTriangleTimesTwo / lenghOfBase;
        return distanceFromPointToRay;
    }

    public static Point intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
        float scaleFactor = rayToPlaneVector.dotProduct(plane.nomal) / ray.vector.dotProduct(plane.nomal);
        Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

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
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
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

    public static class Vector {
        public float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public Vector crossProduct(Vector other) {
            return new Vector(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x);
        }

        public float dotProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector scale(float scale) {
            return new Vector(x * scale, y * scale, z * scale);
        }
    }

    public static class Sphere {
        public Point center;
        public float radius;

        public Sphere(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    /**
     * 平面定义，一个法向向量和平面上一个点
     */
    public static class Plane {
        public Point point;
        public Vector nomal;

        public Plane(Point point, Vector vector) {
            this.point = point;
            this.nomal = vector;
        }
    }
}
