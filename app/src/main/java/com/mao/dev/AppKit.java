package com.mao.dev;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;

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

        String ipAddressString = "";
        try {
            WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(ipAddress);
            }
            byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ipAddressString) && AppKit.isWifiApEnable()) {
            /*
            example:
            https://github.com/CyanogenMod/android_frameworks_base/blob/cm-10.1/wifi/java/android/net/wifi/WifiStateMachine.java?source=c#L1299
            */
            ipAddressString = "192.168.43.1";

        }
        Logger.d("ipAddress:" + ipAddressString);
        return ipAddressString;
    }

    public static boolean isWifiApEnable() {
        try {
            WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            boolean state = (Boolean) method.invoke(wifiManager);
            Logger.d("wifi ap state:" + state);
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap readBitmapFromAsset(String name) {
        AssetManager assetManager = mContext.getAssets();
        InputStream inputStream = null;
        FileOutputStream fos = null;
        InputStream in = null;
        Bitmap bitmap = null;
        File file = new File(getTextStickerFolder(), name);
        try {
            inputStream = assetManager.open(name);
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[128];
            int length = 0;
            while ((length = inputStream.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            FileUtil.unzip(file.getAbsolutePath(), getTextStickerFolder().getAbsolutePath() + "/");
//            bitmap = getBitmapFromFile(name.substring(0, name.lastIndexOf(".")) + ".png");
            in = new FileInputStream(new File(getTextStickerFolder(), "textsticker.png"));
            return decodeBitmap(in, R.drawable.ic_launcher);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private static Bitmap decodeBitmap(InputStream is, int resId) {
        Rect pad = new Rect();
        Resources resources = getResources();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScreenDensity = resources.getDisplayMetrics().densityDpi;
        TypedValue value = new TypedValue();
        resources.getValue(resId, value, false);

        final int density = value.density;
        if (density == TypedValue.DENSITY_DEFAULT) {
            opts.inDensity = DisplayMetrics.DENSITY_DEFAULT;
        } else if (density != TypedValue.DENSITY_NONE) {
            opts.inDensity = density;
        }
        if (opts.inTargetDensity == 0) {
            opts.inTargetDensity = resources.getDisplayMetrics().densityDpi;
        }

        return BitmapFactory.decodeStream(is, pad, opts);
    }

    private static File getTextStickerFolder() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/immomo/textsticker");
    }
}
