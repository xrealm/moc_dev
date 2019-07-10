package com.mao.dev.ui.frame;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/10/24.
 */

public class FrameActivity extends AppCompatActivity {

    private AnimTextureView mAnimTextureView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_test);

        mAnimTextureView = (AnimTextureView) findViewById(R.id.animview);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int[] res = {
//                        R.mipmap.hani_pull_1,
//                        R.mipmap.hani_pull_2,
//                        R.mipmap.hani_pull_3,
//                        R.mipmap.hani_pull_4,
//                        R.mipmap.hani_pull_5,
//                        R.mipmap.hani_pull_6
//                };
//                mAnimTextureView.start(res, 15);

                test();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimTextureView.stop();
            }
        });
    }

    private void test() {
        int[] liveRes = {
                R.mipmap.hani_lable_light_lable_swipe_000,
                R.mipmap.hani_lable_light_lable_swipe_002,
                R.mipmap.hani_lable_light_lable_swipe_003,
                R.mipmap.hani_lable_light_lable_swipe_004,
                R.mipmap.hani_lable_light_lable_swipe_005,
                R.mipmap.hani_lable_light_lable_swipe_006,
                R.mipmap.hani_lable_light_lable_swipe_007,
                R.mipmap.hani_lable_light_lable_swipe_008,
                R.mipmap.hani_lable_light_lable_swipe_010,
                R.mipmap.hani_lable_light_lable_swipe_009,
                R.mipmap.hani_lable_light_lable_swipe_011,
                R.mipmap.hani_lable_light_lable_swipe_012,
                R.mipmap.hani_lable_light_lable_swipe_013,
                R.mipmap.hani_lable_light_lable_swipe_014,
                R.mipmap.hani_lable_light_lable_swipe_015,
                R.mipmap.hani_lable_light_lable_swipe_016,
                R.mipmap.hani_lable_light_lable_swipe_017,
                R.mipmap.hani_lable_light_lable_swipe_018,
                R.mipmap.hani_lable_light_lable_swipe_019,
                R.mipmap.hani_lable_light_lable_swipe_020,
                R.mipmap.hani_lable_light_lable_swipe_021,
                R.mipmap.hani_lable_light_lable_swipe_022,
                R.mipmap.hani_lable_light_lable_swipe_023,
                R.mipmap.hani_lable_light_lable_swipe_024,
                R.mipmap.hani_lable_light_lable_swipe_025,
                R.mipmap.hani_lable_light_lable_swipe_026,
                R.mipmap.hani_lable_light_lable_swipe_027,
                R.mipmap.hani_lable_light_lable_swipe_028,
                R.mipmap.hani_lable_light_lable_swipe_029,
                R.mipmap.hani_lable_light_lable_swipe_030,
                R.mipmap.hani_lable_light_lable_swipe_031
        };

        mAnimTextureView.start(liveRes, 60);
    }
}
