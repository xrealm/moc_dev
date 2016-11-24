package com.mao.dev;

import android.content.Context;

/**
 * Created by Mao on 2016/11/18.
 */

public class AppKit {

    private static Context mContext;

    public static void setApplication(Context context) {
        mContext = context;
    }

    public static int dp2px(int dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dp);
    }
}
