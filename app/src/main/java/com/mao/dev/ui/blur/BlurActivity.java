package com.mao.dev.ui.blur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/4/6.
 */

public class BlurActivity extends AppCompatActivity {

    private FastImageView mFastImageView;
    private FastImageRender mRender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_blur);
        mFastImageView = (FastImageView) findViewById(R.id.glsurfaceview);
        mRender = new FastImageRender(this);
        mFastImageView.setPipleline(mRender);

        findViewById(R.id.blur).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFastImageView.requestRender();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFastImageView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFastImageView.onPause();
    }
}
