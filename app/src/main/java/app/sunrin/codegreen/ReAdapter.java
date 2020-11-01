package app.sunrin.codegreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReAdapter extends RecyclerView.Adapter<ReAdapter.ViewHolder> {

    private ArrayList<Item> listdata = new ArrayList<>();

    ReAdapter(ArrayList<Item> list) {
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
        Item item = listdata.get(position);
        holder.reimg.setImageDrawable(item.getRecycle_img());
        holder.reclass.setText(item.getRecycle_class());
        holder.rehow.setText(item.getRecycle_how());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView reimg;
        private TextView reclass, rehow;

        ViewHolder(View view){
            super(view);

            reimg = view.findViewById((R.id.recycle_img));
            reclass = view.findViewById(R.id.recycle_class);
            rehow = view.findViewById(R.id.recycle_how);
        }

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

}
