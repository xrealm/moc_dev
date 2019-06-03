package com.mao.dev.ui.path;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.mao.dev.R;

/**
 * Created by Mao on 1/8/18.
 */
public class PathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        final PathView pathView = (PathView) findViewById(R.id.pathview);

        TextView tvStart = findViewById(R.id.tv_start);


        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathSet pathSet = new PathSet();

                pathSet.moveTo(100, 100);
                pathSet.quadBezierTo(400,400, 800,200);
                pathSet.quadBezierTo(1000,300, 800,400);
                pathSet.cubicBezierTo(600, 500, 300, 500, 200, 1000);
                pathSet.cubicBezierTo(400, 600, 600, 1200, 800, 1000);
                pathSet.lineTo(800, 1200);

                ValueAnimator animator = ValueAnimator.ofObject(new PathEvaluator(), pathSet.getPathPoints().toArray());
                animator.setDuration(5000);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PathPoint animatedValue = (PathPoint) animation.getAnimatedValue();
                        pathView.setPathPoint(animatedValue);
                    }
                });
                animator.start();
            }
        });


    }
}
