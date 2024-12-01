package com.het.host.test;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public abstract class InfiniteAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected final int MAX_PAGE_COUNT = 5;
    protected final int MIN_INFINITE_COUNT = 3;
    protected Context mContext;
    protected RecyclerView recyclerView;
    protected RecyclerView.ItemDecoration itemDecoration;
    private InfiniteItemClickListener clickListener;
    private int initalPoi;
    protected ArrayList<T> mDatas = new ArrayList<>();

    abstract int getRealItemPosition(int position);

    abstract int getRealItemWidth();

    public InfiniteAdapter(Context context) {
        mContext = context;
    }

    public InfiniteAdapter(Context context, List<T> list) {
        mContext = context;
        mDatas.clear();
        mDatas.addAll(list);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initalPoi = scrollToMiddle();
            }
        },50);
    }

    public void setupData(List<T> list) {
        mDatas.clear();
        mDatas.addAll(list);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initalPoi = scrollToMiddle();
            }
        },20);
        notifyDataSetChanged();
    }

    public void setClickListener(InfiniteItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        addItemDecoration();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.recyclerView.getLayoutParams();
        layoutParams.width = getRealItemWidth()*MAX_PAGE_COUNT;
        this.recyclerView.setLayoutParams(layoutParams);
        InfiniteLayoutManager layoutManager = (InfiniteLayoutManager)this.recyclerView.getLayoutManager();
        layoutManager.curItem = initalPoi;
    }

    @Override
    public int getItemCount() {
        if (mDatas.size()<=MIN_INFINITE_COUNT) {
            return mDatas.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int poi = scrollItemToMiddle(position);
                int realPoi = getRealItemPosition(position);
                InfiniteLayoutManager layoutManager = (InfiniteLayoutManager)recyclerView.getLayoutManager();
                if (layoutManager.curItem == realPoi) {
                    if (clickListener != null) clickListener.onItemClick(realPoi, mDatas.get(realPoi));
                }
                layoutManager.curItem = realPoi;
            }
        });
    }

    public int scrollToMiddle() {
        int size = mDatas.size();
        if (size > MIN_INFINITE_COUNT) {
            // 设置初始位置为数据中间
            int poi = Integer.MAX_VALUE / 2 / mDatas.size();
            poi = poi * mDatas.size();
            int initialPosition = poi + (MIN_INFINITE_COUNT-1);
            if (recyclerView != null) recyclerView.scrollToPosition(initialPosition);
            return initialPosition;
        }
        return 0;
    }

    protected int scrollItemToMiddle(int position) {
        InfiniteLayoutManager layoutManager = (InfiniteLayoutManager) recyclerView.getLayoutManager();
        int firstPoi = layoutManager.findFirstVisibleItemPosition();

        if (mDatas.size()<=MIN_INFINITE_COUNT) {
            if (recyclerView != null) {
                if (position == 1) {
                    layoutManager.scrollToPositionWithOffset(position-1, -getRealItemWidth());
                    if (layoutManager.getChangeListener() != null) layoutManager.getChangeListener().onPageChanged(position);
                } else {
                    recyclerView.smoothScrollToPosition(position);
                }
            }
            return position;
        }

        int poi = position - firstPoi;
        if (poi<MIN_INFINITE_COUNT) {
            poi = poi - (MIN_INFINITE_COUNT-1);
        } else {
            poi = poi + (MIN_INFINITE_COUNT-1);
        }
        if (poi != 0) {
            if (recyclerView != null) recyclerView.smoothScrollToPosition(firstPoi+poi);
        }
        return firstPoi+poi;
    }

    private void addItemDecoration() {
        int itemCount = recyclerView.getItemDecorationCount();
        for (int i = 0; i < itemCount; i++) {
            recyclerView.removeItemDecorationAt(i);
        }
        itemDecoration = null;
        if (mDatas.size() <= MIN_INFINITE_COUNT) {
            itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    int poi = parent.getChildAdapterPosition(view);
                    if (poi == 0) {
                        outRect.left = getRealItemWidth()*2+1;
                    } else if (poi == state.getItemCount()-1) {
                        outRect.right = getRealItemWidth()*2+1;
                    } else {
                        outRect.left = 0;
                        outRect.right = 0;
                    }
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

}
