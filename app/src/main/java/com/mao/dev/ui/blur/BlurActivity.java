package com.mao.dev.ui.blur;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;
import com.mao.dev.fuction.sensor.SensorController;

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

        SensorController.getInstance().start();
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
