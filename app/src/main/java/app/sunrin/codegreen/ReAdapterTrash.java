package app.sunrin.codegreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReAdapterTrash extends RecyclerView.Adapter<ReAdapterTrash.ViewHolder> {

    private ArrayList<ItemTrash> listdata = new ArrayList<>();

    int viewCount = 21;
    int trashLocationCount = 2725;

    ReAdapterTrash(ArrayList<ItemTrash> list) {
        listdata=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycle,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Item item = listdata.get(position);
        ItemTrash item = listdata.get(position);


        for(int i = 0; i<viewCount; i++)
        {
            holder.view[i].setText(item.getRecycle(i));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView[] view;

        ViewHolder(View v){
            super(v);

          //  view[0] = v.findViewById(R.id.);
            //TODO
        }

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

}
