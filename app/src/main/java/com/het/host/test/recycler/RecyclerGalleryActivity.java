package com.het.host.test.recycler;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.het.attendance.test.R;

import java.util.ArrayList;


public class RecyclerGalleryActivity extends Activity implements GalleryLayoutManager.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterLoop adapterLoop;
    private RecyclerViewAdapterNoLoop adapterNoLoop;

    private ArrayList<GalleryBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_gallery_view);
        hideBottomUIMenu();
        //*


        list.add(new GalleryBean(R.drawable.pic0, "标题1", false));
        list.add(new GalleryBean(R.drawable.pic1, "标题2", true));
        list.add(new GalleryBean(R.drawable.pic2, "标题3", false));
        list.add(new GalleryBean(R.drawable.pic3, "标题4", false));
        list.add(new GalleryBean(R.drawable.pic4, "标题5", false));
        list.add(new GalleryBean(R.drawable.pic5, "标题6", false));

        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        adapterLoop = new RecyclerViewAdapterLoop(this, list);
        adapterNoLoop = new RecyclerViewAdapterNoLoop(this, list);


        Button btLoop = findViewById(R.id.bt_loop);
        Button noBtLoop = findViewById(R.id.no_bt_loop);

        btLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1();
            }
        });

        noBtLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test2();
            }
        });

    }

    private void test1() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        recyclerView.setLayoutParams(params);
        GalleryLayoutManager manager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        manager.attach(recyclerView, Integer.MAX_VALUE / 2 - 1);//默认选中 Integer.max = 2147483647
        // 设置滑动缩放效果
        manager.setItemTransformer(new Transformer());

        recyclerView.setAdapter(adapterLoop);
        manager.setOnItemSelectedListener(this);
        adapterLoop.setmOnItemClickListener(new RecyclerViewAdapterLoop.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, GalleryBean item) {
                // 支持手动点击滑动切换
                recyclerView.smoothScrollToPosition(Integer.valueOf(data));
            }
        });
    }

    private void test2() {

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);
        recyclerView.setLayoutParams(params);
        // 获取 ConstraintLayout 实例
        ConstraintLayout constraintLayout = findViewById(R.id.container);

        // 创建一个 ConstraintSet 实例
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // 获取要居中的控件的 ID
        int viewId = R.id.recyclerView;

        // 将控件水平居中
        constraintSet.centerHorizontally(viewId, ConstraintSet.PARENT_ID);
        // 将控件垂直居中
        // constraintSet.centerVertically(viewId, ConstraintSet.PARENT_ID);
        // 应用约束布局的更改
        constraintSet.applyTo(constraintLayout);


        recyclerView.setAdapter(adapterNoLoop);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapterNoLoop.setmOnItemClickListener(new RecyclerViewAdapterNoLoop.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, GalleryBean item) {
                adapterNoLoop.itemClickNoLoopModel(recyclerView, Integer.valueOf(data));
            }
        });
    }

    GalleryBean adapterLoopChecked;

    @Override
    public void onItemSelected(RecyclerView recyclerView, View item, int position) {
        //无线循环 滑动监听    选中
        adapterLoopChecked = adapterLoop.getBindItemData(position); //选中的数据
    }


    private GalleryBean selectedData() {
        if (list.size() >= 3) {
            return adapterLoopChecked;
        } else {
            return adapterNoLoop.checked();
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
