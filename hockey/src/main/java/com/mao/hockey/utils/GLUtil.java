package com.mao.hockey.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Mao on 2017/3/19.
 */

public class GLUtil {

    public static String readShaderFromRaw(Context context, int resId) {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resId)));
            String line = "";
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(br);
        }
        return builder.toString();
    }
}
