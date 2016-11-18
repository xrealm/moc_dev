package com.mao.dev;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mao on 2016/11/18.
 */

public class WatermarkTimerManager {
    private static final int DELAY_DURATION = 3000;
    private static final int PERIOD_DURATION = 5000;

    private WatermarkUpdateCallBack mUpdateCallBack;
    private Timer mTimer;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mUpdateCallBack.watermarkUpdate();
        }
    };

    public WatermarkTimerManager(WatermarkUpdateCallBack callBack) {
        mTimer = new Timer();
        mUpdateCallBack = callBack;
    }

    public void start() {
        mTimer.schedule(mTimerTask, DELAY_DURATION, PERIOD_DURATION);
        mUpdateCallBack.watermarkUpdate();
    }

    public void pause() {
        mTimer.cancel();
    }

    public void resume() {
        mTimer.cancel();
        mTimer = new Timer();
        start();
    }

    public void stop() {
        mTimer.cancel();
        mTimer = null;
    }

    public interface WatermarkUpdateCallBack {
        void watermarkUpdate();
    }
}
