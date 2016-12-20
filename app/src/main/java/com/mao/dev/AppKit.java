package com.mao.dev;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

    public static Resources getResources() {
        return mContext.getResources();
    }

    public static NinePatchDrawable getNinePatchDrawableFromCache(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        byte[] chunk = bitmap.getNinePatchChunk();
        boolean ninePatchChunk = NinePatch.isNinePatchChunk(chunk);
        if (ninePatchChunk) {
            Rect paddingRect = new Rect();
            readPaddingFromChunk(chunk, paddingRect);
            paddingRect.top = dp2px(10);
            paddingRect.bottom = dp2px(8);
            return new NinePatchDrawable(getResources(), bitmap, chunk, paddingRect, null);
        } else {
            return null;
        }
    }

    private static void readPaddingFromChunk(byte[] chunk, Rect paddingRect) {
        paddingRect.left = Array.getInt(chunk, 12);
        paddingRect.right = Array.getInt(chunk, 16);
        paddingRect.top = Array.getInt(chunk, 20);
        paddingRect.bottom = Array.getInt(chunk, 24);
    }

    public static Bitmap decodeBitmapByStream(String path) {
        InputStream is = null;
        try {
            File file = new File(path);
            is = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Bitmap decodeFile(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }


    public static String getHostIp() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        Log.d("mao", "host=" + inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e("mao", "SocketException in get Ip Adress");
        }
        return "";
    }

    public static int getAvailablePort() {
        return 0;
    }
}
