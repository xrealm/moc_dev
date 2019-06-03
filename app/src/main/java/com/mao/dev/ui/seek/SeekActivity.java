package com.mao.dev.ui.seek;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mao.dev.R;
import com.orhanobut.logger.Logger;

/**
 * Created by Mao on 2017/7/12.
 */

public class SeekActivity extends AppCompatActivity {

    TextView tvProgress;
    SearchView mSearchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);

//        TwoWaySeekBar seekBar = (TwoWaySeekBar) findViewById(R.id.seekbar);
//        tvProgress = (TextView) findViewById(R.id.tv_progress);
//        seekBar.setOnSeekBarChangeListener(new TwoWaySeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(TwoWaySeekBar seekBar, int touchX, int progress, boolean fromUser) {
//                Logger.d("xx = " + touchX);
//                tvProgress.setText("test:" + progress);
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tvProgress.getLayoutParams();
//                params.leftMargin = seekBar.getLeft() + touchX - tvProgress.getWidth() / 2;
//                tvProgress.requestLayout();
//            }
//
//            @Override
//            public void onStartTrackingTouch(TwoWaySeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(TwoWaySeekBar seekBar) {
//
//            }
//        });

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.i("query= " + query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.i("newText= " + newText);
                return true;
            }
        });
    }
}
