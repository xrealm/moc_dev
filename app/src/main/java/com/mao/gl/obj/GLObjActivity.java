package com.mao.gl.obj;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/5/16.
 */

public class GLObjActivity extends AppCompatActivity {

    private GLSurfaceView mGlSurfaceView;
    private HaHaRender mRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_obj);
        mGlSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        mGlSurfaceView.setEGLContextClientVersion(2);
        mRender = new HaHaRender(this);
        mGlSurfaceView.setRenderer(mRender);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }
}
