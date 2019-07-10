package com.mao.gl.nativedemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/10/23.
 */

public class GLNativeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl_native);
        GLSurfaceView surfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        surfaceView.setEGLContextClientVersion(2);
        MyRender render = new MyRender(this);
        surfaceView.setRenderer(render);
    }
}
