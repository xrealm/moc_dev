package com.mao.dev.ui.voice;

import java.math.BigDecimal;

/**
 * Created by Mao on 2017/2/21.
 */

public class Airth {

    private static final int DEF_DIV_SCALE = 10;

    private Airth() {

    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    private static double div(double v1, double v2, int defDivScale) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, defDivScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b1.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
