package com.mao.dev.ui.path;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * Created by Mao on 1/8/18.
 */
public class PathEvaluator implements TypeEvaluator<PathPoint> {

    PathPoint mPoint;

    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
        if (mPoint == null || !mPoint.equals(startValue)) {
            Log.w("mao", "startValue: " + startValue.toString());
            mPoint = startValue;
        }
        Log.d("mao", "fraction: " + fraction);
        float x, y;
        float remain = 1 - fraction;

        //三阶贝塞尔曲线
        if (endValue.operation == PathPoint.CUBIC_CURVE) {
            x = startValue.x * remain * remain * remain
                    + 3 * endValue.c0x * fraction * remain * remain
                    + 3 * endValue.c1x * fraction * fraction * remain
                    + endValue.x * fraction * fraction * fraction;
            y = startValue.y * remain * remain * remain
                    + 3 * endValue.c0y * fraction * remain * remain
                    + 3 * endValue.c1y * fraction * fraction * remain
                    + endValue.y * fraction * fraction * fraction;
            //二阶贝塞尔曲线
        } else if (endValue.operation == PathPoint.QUAD_CURVE) {
            x = remain * remain * startValue.x
                    + 2 * fraction * remain * endValue.c0x
                    + fraction * fraction * endValue.x;
            y = remain * remain * startValue.y
                    + 2 * fraction * remain * endValue.c0y
                    + fraction * fraction * endValue.y;
            //直线
        } else if (endValue.operation == PathPoint.LINE) {
            // 起始点 + t*起始点和终点的距离
            x = startValue.x + fraction * (endValue.x - startValue.x);
            y = startValue.y + fraction * (endValue.y - startValue.y);
        } else {
            x = endValue.x;
            y = endValue.y;
        }
        return PathPointFactory.movePoint(x, y);
    }
}
