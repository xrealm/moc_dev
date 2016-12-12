package com.mao.dev.ui.rv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mao.dev.R;
import com.mao.dev.WatermarkTimerManager;

/**
 * Created by Mao on 2016/11/14.
 */

public class GridActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView mTextView;
    private int mCount;
    private WatermarkTimerManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvgrid);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration(3, 50));
        mRecyclerView.setAdapter(new MyAdapter());

        mTextView = (TextView) findViewById(R.id.tv_test);
        mManager = new WatermarkTimerManager(new WatermarkTimerManager.WatermarkUpdateCallBack() {
            @Override
            public void watermarkUpdate() {
                mTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(String.valueOf(mCount++));
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mManager.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mManager.stop();
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 15;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
