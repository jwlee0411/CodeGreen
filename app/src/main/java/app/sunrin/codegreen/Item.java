package app.sunrin.codegreen;

import android.graphics.drawable.Drawable;

public class Item {
    private Drawable recycle_img;
    private String recycle_class;
    private String recycle_how;

    public Drawable getRecycle_img() {
        return recycle_img;
    }

    public void setRecycle_img(Drawable recycle_img) {
        this.recycle_img = recycle_img;
    }

    public String getRecycle_class() {
        return recycle_class;
    }

    public void setRecycle_class(String recycle_class) {
        this.recycle_class = recycle_class;
    }

    public String getRecycle_how() {
        return recycle_how;
    }

    public void setRecycle_how(String recycle_how) {
        this.recycle_how = recycle_how;
    }

    public Item() {
        this.recycle_img = recycle_img;
        this.recycle_class = recycle_class;
        this.recycle_how = recycle_how;
    }
}
