package com.mao.dev.ui.blur;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Mao on 2017/4/6.
 */

public class FastImageView extends GLSurfaceView{

    public FastImageView(Context context) {
        super(context);
        init();
    }

    public FastImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);
        setEGLContextClientVersion(2);
    }

    public void setPipleline(FastImageRender render) {
        setRenderer(render);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
