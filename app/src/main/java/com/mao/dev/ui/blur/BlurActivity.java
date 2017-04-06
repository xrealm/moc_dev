package com.mao.dev.ui.blur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/4/6.
 */

public class BlurActivity extends AppCompatActivity {

    private FastImageView fastImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_blur);
        fastImageView = (FastImageView) findViewById(R.id.glsurfaceview);

    }
}
