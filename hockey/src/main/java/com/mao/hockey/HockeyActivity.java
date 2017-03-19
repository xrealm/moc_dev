package com.mao.hockey;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        glSurfaceView.setRenderer(new HockeyRender(this));
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
