package com.mao.dev.ui.progress;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/6/13.
 */

public class ProgressActivity extends AppCompatActivity {

    private SquareProgressView mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mProgressView = (SquareProgressView) findViewById(R.id.square_progress);
        mProgressView.setWidthInDp(2);
        mProgressView.setTimerProgress(80, 30_000);
//        mProgressView.setProgress(60);
        mProgressView.setBaseBar(true);
//        setCurrentProgress();
        findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressView.setTimerProgress(80, 30_000);
            }
        });

    }

    ObjectAnimator mAnimator;
    private void setCurrentProgress() {
        if (mAnimator != null) {
            mAnimator.cancel();
//            mAnimator.end();
            mAnimator = null;
        }
        mAnimator = ObjectAnimator.ofFloat(mProgressView, "progress", 100, 0)
                .setDuration(30_000);
        mAnimator.start();
    }
}
