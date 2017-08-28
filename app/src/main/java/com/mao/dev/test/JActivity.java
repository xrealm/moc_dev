package com.mao.dev.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.mao.dev.R;

/**
 * Created by Mao on 2017/8/25.
 */

public class JActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_test_activity);

        final TextView textView = (TextView) findViewById(R.id.tv_jtest);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.btn_codec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(FFDemo.avcodecinfo());
            }
        });

    }
}
