package com.mao.hockey;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;

public class HockeyActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init("mao---")
                .methodCount(0)
                .methodOffset(1);
        setContentView(R.layout.activity_hockey);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surfaceview);
        glSurfaceView.setEGLContextClientVersion(2);
        final HockeyRender render = new HockeyRender(this);
        glSurfaceView.setRenderer(render);
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //转换为归一化设备坐标[-1,1]
                final float nomalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                final float nomalizedY = (event.getY() / (float) v.getHeight()) * 2 - 1;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                render.handleTouchPress(nomalizedX, nomalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_MOVE:
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                render.handleTouchDrag(nomalizedX, nomalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                render.handleTouchUp(nomalizedX, nomalizedY);
                            }
                        });
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
