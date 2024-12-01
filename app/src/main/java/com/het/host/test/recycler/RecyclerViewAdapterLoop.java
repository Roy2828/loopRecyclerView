package com.het.host.test.recycler;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;


public class RecyclerViewAdapterLoop extends BaseGalleryAdapter {


    public RecyclerViewAdapterLoop(Context mContext, List<GalleryBean> list) {
        super(mContext, list);
    }

    @Override
    void optionOnCreateView(View view) {
        // 自定义view的宽度,控制一屏显示的个数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        params.width = width / 5;   //这里每屏显示3个<将屏幕平均分为3份
        view.setLayoutParams(params);
    }

    @Override
    void optionOnBindView(RecyclerViewHolder holder, int position, GalleryBean item) {

    }


    @Override
    GalleryBean getBindItemData(int position) {
        return  list.get(position % list.size());
    }


    @Override
    int getChildItemCount() {
        return Integer.MAX_VALUE;
    }


}
