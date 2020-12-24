package app.sunrin.codegreen;

import android.graphics.drawable.Drawable;

public class ItemTrash {

    int viewCount = 21;
    int trashLocationCount = 2725;

    private String[] recycle = new String[viewCount];

    public String getRecycle(int index){
        return recycle[index];
    }

    public void setRecycle(String value, int index)
    {
        this.recycle[index] = value;
    }


    public ItemTrash() {
        for(int i = 0; i<viewCount; i++)
        {
            this.recycle[i] = recycle[i];
        }

    }
}
