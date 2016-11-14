package com.mao.dev.rv;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Mao on 2016/11/14.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private int mSpanCount;
    private Drawable mDivider;

    GridItemDecoration(int spanCount, int space) {
        mSpanCount = spanCount;
        mSpace = space;
        if (spanCount <= 0) {
            mSpanCount = 1;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        outRect.top = position < mSpanCount ? 0 : mSpace;
        outRect.left = position % mSpanCount == 0 ? 0 : mSpace;
    }
}
