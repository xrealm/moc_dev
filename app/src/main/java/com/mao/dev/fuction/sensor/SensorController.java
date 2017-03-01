package com.mao.dev.fuction.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.immomo.molive.foundation.util.MoliveKit;

/**
 * Created by Mao on 2017/2/28.
 */

public class SensorController implements SensorEventListener {

    public static final int DELAY_DURATION = 500;

    public static final int STATUS_NONE = 0;
    public static final int STATUS_STATIC = 1;
    public static final int STATUS_MOVE = 2;

    private int mStatus = STATUS_NONE;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean canFocus;
    private boolean isFocusing;

    private long mLastStamp;
    private int mLastX, mLastY, mLastZ;
    private boolean canFocusIn;
    private CameraFocusListener mFocusListener;

    private SensorController() {
        mSensorManager = (SensorManager) MoliveKit.getAppContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static SensorController getInstance() {
        return SensorHolder.sensorController;
    }

    public void start() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        canFocus = true;
    }

    public void stop() {
        mSensorManager.unregisterListener(this, mSensor);
        canFocus = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }
        Log.d("mao--->", "isFocusing=" + isFocusing);
        if (isFocusing) {
            reset();
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

            long stamp = System.currentTimeMillis() / 1000;

            if (mStatus == STATUS_NONE) {
                int dx = Math.abs(mLastX - x);
                int dy = Math.abs(mLastY - y);
                int dz = Math.abs(mLastZ - z);

                double value = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (value > 1.4) {
                    Log.d("mao--->", "手机移动...");
                    mStatus = STATUS_MOVE;
                } else {
                    Log.d("mao--->", "手机静止...");
                    if (mStatus == STATUS_MOVE) {
                        mLastStamp = stamp;
                        canFocusIn = true;
                    }

                    if (canFocusIn) {
                        if (stamp - mLastStamp > DELAY_DURATION) {
                            if (!isFocusing) {
                                Log.d("mao--->", "需要对焦");
                                if (mFocusListener != null) {
                                    mFocusListener.onFocus();
                                }
                                canFocusIn = false;
                            }
                        }
                    }
                    mStatus = STATUS_STATIC;
                }
            } else {
                mLastStamp = stamp;
                mStatus = STATUS_STATIC;
            }
            mLastX = x;
            mLastY = y;
            mLastZ = z;
        }
    }

    private void reset() {
        mLastX = 0;
        mLastY = 0;
        mLastZ = 0;
        mStatus = STATUS_NONE;
        canFocusIn = false;
    }

    public void lockFocus() {
        isFocusing = true;
    }

    public void unLockFocus() {
        isFocusing = false;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static class SensorHolder {
        private static SensorController sensorController = new SensorController();
    }

    public void setCameraFocusListener(CameraFocusListener listener) {
        mFocusListener = listener;
    }

    public interface CameraFocusListener {
        void onFocus();
    }
}
