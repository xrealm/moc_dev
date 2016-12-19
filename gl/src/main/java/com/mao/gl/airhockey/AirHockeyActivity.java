package com.mao.gl.airhockey;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.mao.gl.R;
import com.mao.gl.airhockey.render.AirHockeyRender;

/**
 * Created by Mao on 16/10/10.
 */

public class AirHockeyActivity extends AppCompatActivity {

    GLSurfaceView mGLSurfaceView;
    private AirHockeyRender mAirHockeyRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_hockey);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surfaceview);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mAirHockeyRender = new AirHockeyRender(this);
        mGLSurfaceView.setRenderer(mAirHockeyRender);

        mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //事件坐标映射到[-1,1]
                final float nomalizedX = event.getX() / (float) v.getWidth() * 2 - 1;
                final float nomalizedY = event.getY() / (float) v.getHeight() * 2 - 1;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRender.handleTouchPress(nomalizedX, nomalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRender.handleTouchDrag(nomalizedX, nomalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
