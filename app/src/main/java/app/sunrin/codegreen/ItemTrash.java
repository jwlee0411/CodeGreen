package app.sunrin.codegreen;

import android.graphics.drawable.Drawable;

public class ItemTrash {

    private String[] recycle;

    public String getRecycle(int index){
        return recycle[index];
    }

    public void setRecycle(String value, int index)
    {
        this.recycle[index] = value;
    }


    public ItemTrash() {
        for(int i = 0; i<25; i++)
        {
            this.recycle[i] = recycle[i];
        }

    }
}
