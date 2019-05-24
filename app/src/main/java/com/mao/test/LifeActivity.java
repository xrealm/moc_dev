package com.mao.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


/**
 * Created by Mao on 2019-05-24.
 */
public class LifeActivity extends AppCompatActivity {

    private static final String TAG = "mao";

    protected String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, getName() + " onCreate: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, getName() + " onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, getName() + " onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, getName() + " onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, getName() + " onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, getName() + " onDestroy: ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, getName() + " onNewIntent: ");
    }
}
