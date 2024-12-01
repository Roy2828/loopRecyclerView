package com.het.host.test.recycler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.het.attendance.test.R;

import java.util.List;

public abstract class BaseGalleryAdapter extends  RecyclerView.Adapter<RecyclerViewHolder> {


    List<GalleryBean> list;

    protected Context mContext;
    protected   OnRecyclerViewItemClickListener mOnItemClickListener = null;


    public BaseGalleryAdapter(Context mContext,List<GalleryBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_gallery_view, null);
           optionOnCreateView(view);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.img);
        TextView textView = holder.itemView.findViewById(R.id.text);

        GalleryBean item =  getBindItemData(position);
        imageView.setImageResource(item.getImg());
        textView.setText("img_0" + item.getTitle());
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recycler.smoothScrollToPosition(position); 支持点击每一项滑动切换
                if (mOnItemClickListener != null) {

                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(v, v.getTag().toString(),item);
                }

            }
        });
        optionOnBindView(holder,position,item);
    }

    abstract GalleryBean getBindItemData(int position);


    abstract  void optionOnCreateView(View view);
    abstract void  optionOnBindView(RecyclerViewHolder holder, int position, GalleryBean item);

    @Override
    public int getItemCount() {
        return getChildItemCount();
    }


    abstract int getChildItemCount();



    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //定义一个接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data, GalleryBean item);
    }
}
