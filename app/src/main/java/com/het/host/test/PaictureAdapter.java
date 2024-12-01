package com.het.host.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.het.attendance.test.R;

import java.util.List;

public class PaictureAdapter extends InfiniteAdapter<Integer> {

    public PaictureAdapter(Context context) {
        super(context);
    }

    public PaictureAdapter(Context context, List<Integer> list) {
        super(context, list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img,parent,false);
        ViewHolder itemViewHodler = new ViewHolder(view);
        return itemViewHodler;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        int poi = getRealItemPosition(position);
       ImageView img = holder.itemView.findViewById(R.id.img);
       img.setImageResource(mDatas.get(poi));
    }

    @Override
    int getRealItemPosition(int position) {
        int virtualPosition = position % mDatas.size();
        return virtualPosition;
    }

    @Override
    int getRealItemWidth() {
        return DisplayUtil.dp2px(100f);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
