package com.mao.gl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mao.dev.R;
import com.mao.gl.obj.GLObjActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 2017/5/10.
 */

public class GLMianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MenuAdapter adapter = new MenuAdapter(generateMenuList());
        recyclerView.setAdapter(adapter);
    }

    private List<Menu> generateMenuList() {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("绘制形体", GLObjActivity.class));
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private List<Menu> data;

        public MenuAdapter(List<Menu> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(GLMianActivity.this);
            textView.setTextColor(0xFF2B2B2B);
            textView.setTextSize(14);
            textView.setBackgroundColor(0xff666666);
            textView.setPadding(30, 30, 30, 30);
            textView.setGravity(Gravity.CENTER);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 20;
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                tv= (TextView) itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Menu menu = data.get(getLayoutPosition());
                        startActivity(new Intent(GLMianActivity.this, menu.clazz));
                    }
                });
            }

            public void setData(Menu menu) {
                tv.setText(menu.name);
            }
        }
    }

    private static class Menu {
        String name;
        Class<?> clazz;

        public Menu(String name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }
}
