package app.sunrin.codegreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
                .inflate(R.layout.item_trash,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemTrash item = listdata.get(position);


        for(int i = 0; i<viewCount; i++)
        {
            holder.view[i].setText(item.getRecycle(i));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView[] view = new TextView[viewCount];

        ViewHolder(View v){
            super(v);

            view[0] = v.findViewById(R.id.textLocation);
            view[1] = v.findViewById(R.id.textCall);
            view[2] = v.findViewById(R.id.textDataDate);
            view[3] = v.findViewById(R.id.textNoTrash);
            view[4] = v.findViewById(R.id.textTrashLocation);
            view[5] = v.findViewById(R.id.textLifeTrashWay);
            view[6] = v.findViewById(R.id.textLifeTrashDay);
            view[7] = v.findViewById(R.id.textLifeTrashStart);
            view[8] = v.findViewById(R.id.textLifeTrashEnd);
            view[9] = v.findViewById(R.id.textFoodTrashWay);
            view[10] = v.findViewById(R.id.textFoodTrashDay);
            view[11] = v.findViewById(R.id.textFoodTrashStart);
            view[12] = v.findViewById(R.id.textFoodTrashEnd);
            view[13] = v.findViewById(R.id.textManyTrashWay);
            view[14] = v.findViewById(R.id.textManyTrashDay);
            view[15] = v.findViewById(R.id.textManyTrashStart);
            view[16] = v.findViewById(R.id.textManyTrashEnd);
            view[17] = v.findViewById(R.id.textRecycleWay);
            view[18] = v.findViewById(R.id.textRecycleDay);
            view[19] = v.findViewById(R.id.textRecycleStart);
            view[20] = v.findViewById(R.id.textRecycleEnd);


        }

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

}
