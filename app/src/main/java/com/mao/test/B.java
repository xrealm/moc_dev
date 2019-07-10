package com.mao.test;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.mao.dev.R;


/**
 * Created by Mao on 2019-05-24.
 */
public class B extends LifeActivity {

    @Override
    protected String getName() {
        return "B";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B.this, C.class);
                startActivity(intent);
            }
        });
    }
}
