package com.eyupyilmaz.needyourbag.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eyupyilmaz.needyourbag.Constants.MyConstants;
import com.eyupyilmaz.needyourbag.Database.RoomDB;
import com.eyupyilmaz.needyourbag.Models.Items;
import com.eyupyilmaz.needyourbag.R;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    Context context;
    List<Items> itemsList;
    RoomDB database;
    String show;

    public CheckListAdapter() {
    }

    public CheckListAdapter(Context context, List<Items> itemsList, RoomDB database, String show) {
        this.context = context;
        this.itemsList = itemsList;
        this.database = database;
        this.show = show;
        if (itemsList.size() == 0)
            Toast.makeText(context.getApplicationContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckListViewHolder(LayoutInflater.from(context).inflate(R.layout.check_list_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        holder.checkBox.setText(itemsList.get(position).getItemname());
        holder.checkBox.setChecked(itemsList.get(position).getChecked());


        if (MyConstants.FALSE_STRING.equals(show)){
            holder.btnDelete.setVisibility(View.GONE);
            holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
        }else{
            if (itemsList.get(position).getChecked()){
                holder.layout.setBackgroundColor(Color.parseColor("#8e546f"));
            }else {
                holder.layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_one));
            }
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = holder.checkBox.isChecked();
                database.mainDao().checkUnCheck(itemsList.get(position).getID(),check);
                if (MyConstants.FALSE_STRING.equals(show)){
                    itemsList = database.mainDao().getAllSelected(true);
                    notifyDataSetChanged();
                }else {
                    itemsList.get(position).setChecked(check);
                    notifyDataSetChanged();
                    Toast toastMessage = null;
                    if (toastMessage != null){
                        toastMessage.cancel();
                    }

                    if (itemsList.get(position).getChecked()){
                        toastMessage = Toast.makeText(context,"("+holder.checkBox.getText()+") Packed",Toast.LENGTH_SHORT);
                    }else {
                        toastMessage = Toast.makeText(context,"("+holder.checkBox.getText()+") Un-Packed",Toast.LENGTH_SHORT);
                    }
                    toastMessage.show();
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete ( "+itemsList.get(position).getItemname()+" )")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.mainDao().delete(itemsList.get(position));
                                itemsList.remove(itemsList.get(position));
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        }).setIcon(R.drawable.ic_delete_forever).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}


    class CheckListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        CheckBox checkBox;
        Button btnDelete;
        public CheckListViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linearLayout);
            checkBox = itemView.findViewById(R.id.checkbox);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

