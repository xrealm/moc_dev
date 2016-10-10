package com.mao.gl.airhockey;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.gl.R;
import com.mao.gl.airhockey.render.AirHockeyRender;

/**
 * Created by Mao on 16/10/10.
 */

public class AirHockeyActivity extends AppCompatActivity {

    GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_hockey);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surfaceview);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(new AirHockeyRender());
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
