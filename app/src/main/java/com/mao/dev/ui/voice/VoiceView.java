package com.mao.dev.ui.voice;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mao on 2017/2/20.
 */

public class VoiceView extends View {

    private static final int WAVE = 0x1;
    private static final int LINE = 0x1 << 1;

    private int mode;
    private Paint mPaint;
    private Paint mVoicePaint;

    private List<Rect> mRects;
    private float rectWidth = 25;
    private float rectSpace = 5;
    private float rectInitHeight = 4;
    private long speedY = 50;

    private boolean isSet;
    private int volume = 10;
    private float targetVolume = 1;
    private float maxVolume = 100;
    //灵敏度
    private int sensibility = 4;

    public VoiceView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mVoicePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVoicePaint.setColor(Color.RED);
        mVoicePaint.setStyle(Paint.Style.STROKE);
        mVoicePaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mode & WAVE) != 0) {
            drawVoiceWave(canvas);
        } else {
            drawAxis(canvas);
            drawVoiceLine(canvas);
        }
        run();
    }

    private void drawAxis(Canvas canvas) {
        canvas.save();
        canvas.drawRect(0, getHeight() / 2 - 5, getWidth(), getHeight() / 2 + 5, mPaint);
        canvas.restore();
    }

    private void drawVoiceWave(Canvas canvas) {

    }

    private void drawVoiceLine(Canvas canvas) {
        if (mRects == null) {
            mRects = new LinkedList<>();
        }

        int totalWidth = (int) (rectSpace + rectWidth);
        if (speedY % totalWidth < 6) {
            Rect rect = new Rect((int) (-rectWidth - 10 - speedY + speedY % totalWidth),
                    (int) (getHeight() / 2 - rectInitHeight / 2 - (volume == 10 ? 0 : volume / 2)),
                    (int) (-10 - speedY + speedY % totalWidth),
                    (int) (getHeight() / 2 + rectInitHeight / 2 + (volume == 10 ? 0 : volume / 2)));
            if (mRects.size() > getWidth() / (rectSpace + rectWidth) + 2) {
                mRects.remove(0);
            }
            mRects.add(rect);
        }
        canvas.translate(speedY, 0);
        for (Rect rect : mRects) {
            canvas.drawRect(rect, mVoicePaint);
        }
        changeRect();
    }

    private void changeRect() {
        speedY += 6;
        if (volume < targetVolume && isSet) {
            volume += getHeight() / 30;
        } else {
            isSet = false;
            if (volume <= 10) {
                volume = 10;
            } else {
                if (volume < getHeight() / 30) {
                    volume -= getHeight() / 60;
                } else {
                    volume -= getHeight() / 30;
                }
            }
        }
    }

    private void changeLine() {

    }

    private void run() {
        if ((mode & WAVE) != 0) {

        } else {
            postInvalidateDelayed(30);
        }
    }

    public void setMode(int m) {
        mode = mode & m;
    }

    public void setVoice(int db) {
        if (db > maxVolume * sensibility / 25) {
            isSet = true;
            targetVolume = getHeight() * db / 2 / maxVolume;
        }
        volume = db;
    }
}
