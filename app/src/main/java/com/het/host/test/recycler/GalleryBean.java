package com.het.host.test.recycler;


public class GalleryBean {
   private int img;
   private String title;

   private boolean isChecked;//只针对 小于5的选中状态

    public GalleryBean(int img, String title,boolean isChecked) {
        this.img = img;
        this.title = title;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
