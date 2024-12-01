package com.het.host.test;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class InfiniteLayoutManager extends LinearLayoutManager {
    private RecyclerView recyclerView;
    private PagerSnapHelper snapHelper;
    private InfinitePageChangeListener changeListener;
    private RecyclerView.ItemDecoration itemDecoration;
    public int curItem = -1;
    public InfiniteLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public InfiniteLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public InfiniteLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setChangeListener(InfinitePageChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public InfinitePageChangeListener getChangeListener() {
        return changeListener;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        if (recyclerView != view) {
            recyclerView = view;
            snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(view);
            recyclerView.clearOnScrollListeners();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (RecyclerView.SCROLL_STATE_IDLE == newState && getChildCount() > 0) {
                        View v = snapHelper.findSnapView(InfiniteLayoutManager.this);
                        if (v == null) return;
                        int pos = getPosition(v);
                        InfiniteAdapter adapter = (InfiniteAdapter) recyclerView.getAdapter();
                        int realPoi = adapter.getRealItemPosition(pos);
                        if (pos >= 0) {
                            curItem = realPoi;
                            if (changeListener != null) {
                                changeListener.onPageChanged(curItem);
                            }
                        }
                    }
                }
            });
        }
    }

    public void scrollPreItem() {
        View v = snapHelper.findSnapView(InfiniteLayoutManager.this);
        if (v == null) return;
        int pos = getPosition(v);
        pos = Math.max(0, pos-1);
        InfiniteAdapter adapter = (InfiniteAdapter) recyclerView.getAdapter();
        adapter.scrollItemToMiddle(pos);
    }

    public void scrollNextItem() {
        View v = snapHelper.findSnapView(InfiniteLayoutManager.this);
        if (v == null) return;
        int pos = getPosition(v);
        pos = Math.min(getItemCount()-1, pos+1);
        InfiniteAdapter adapter = (InfiniteAdapter) recyclerView.getAdapter();
        adapter.scrollItemToMiddle(pos);
    }
}
