package com.het.host.test.recycler;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecyclerViewAdapterNoLoop extends BaseGalleryAdapter {


    public RecyclerViewAdapterNoLoop(Context mContext, List<GalleryBean> list) {
        super(mContext, list);
    }

    @Override
    void optionOnCreateView(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        params.width = width /5;   //这里每屏显示3个<将屏幕平均分为5份
        view.setLayoutParams(params);
    }

    @Override
    void optionOnBindView(RecyclerViewHolder holder, int position, GalleryBean item) {
        animScaleView(holder.itemView,item,false);
    }

    @Override
    int getChildItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }


    @Override
    GalleryBean getBindItemData(int position) {
        return  list.get(position);
    }

    private void animScaleView(View itemView,GalleryBean item,boolean isAnim){
        if(item.isChecked()) {
            scaleView(itemView, 0,isAnim); //放大
        }else{
            scaleView(itemView, 1,isAnim); //放大
        }
    }

    public void itemClickNoLoopModel(RecyclerView recyclerView,int position){

        for (int i = 0; i < list.size(); i++) {
            GalleryBean item =  list.get(i);
            if(i == position){
                item.setChecked(true);
            }else{
                item.setChecked(false);
            }


            RecyclerView.ViewHolder viewHolder =    recyclerView.findViewHolderForAdapterPosition(i);
            if(viewHolder != null){
                RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
                if(item != null){
                    animScaleView(recyclerViewHolder.itemView,item,true);
                }
            }
        }


    }



/*    private void scaleView(View item,float fraction){
        //以圆心进行缩放
       // item.setPivotX(item.getWidth() / 2.0f);
      //  item.setPivotY(item.getHeight() / 2.0f);
        float scale = 1 - 0.433f * Math.abs(fraction); // 0.433f是放大的View面积和缩小的View面积的比值
        item.setScaleX(scale);
        item.setScaleY(scale);

    }*/

    public void scaleView(View item, float fraction,boolean isAnim) {
        // 计算缩放比例
        float scale = 1 - 0.433f * Math.abs(fraction); // 0.433f是放大的View面积和缩小的View面积的比值

        if(isAnim) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(item, "scaleX", scale);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(item, "scaleY", scale);
            scaleXAnimator.setDuration(200);
            scaleYAnimator.setDuration(200);
            scaleXAnimator.start();
            scaleYAnimator.start();
        }else{
            item.setScaleX(scale);
            item.setScaleY(scale);
        }
    }


    public GalleryBean checked(){
        for (int i = 0; i < list.size(); i++) {
         GalleryBean bean =    list.get(i);
         if(bean.isChecked()){
             return bean;
         }
        }
        return null;
    }


}
