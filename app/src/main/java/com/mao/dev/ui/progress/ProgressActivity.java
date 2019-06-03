package com.mao.dev.ui.progress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/6/13.
 */

public class ProgressActivity extends AppCompatActivity {

    private SquareProgressView mSquareProgressView;
    private CircleProgressView mCircleProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress);
        mSquareProgressView = (SquareProgressView) findViewById(R.id.square_progress);

        findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSquareProgressView.setTimerProgress(80, 30_000);
                setupCircleProgress();
            }
        });

    }

    private void setupCircleProgress() {
        mCircleProgressView.setupProgress();
    }

    private void setupSquareProgress() {
        mSquareProgressView.setWidthInDp(2);
        mSquareProgressView.setTimerProgress(90, 30_000);
//        mSquareProgressView.setProgress(60);
        mSquareProgressView.setBaseBar(true);
    }
}
