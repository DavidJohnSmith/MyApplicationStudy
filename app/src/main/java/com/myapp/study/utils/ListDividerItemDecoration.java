package com.myapp.study.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lvgy on 16/8/2.
 */
public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int miSpace;

    public ListDividerItemDecoration(int iSpace) {
        miSpace = iSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = miSpace;

    }
}
