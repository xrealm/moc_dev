package com.mao.gl;

import android.content.Context;
import androidx.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mao on 2017/5/10.
 */

public class OpenGLUtil {

    public static String readShaderFromRawResouce(Context context, @RawRes int resId) {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            InputStream is = context.getResources().openRawResource(resId);
            br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = br.readLine()) != null) {
                builder.append(line)
                        .append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br!=null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
