package com.example.test.MenuPages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<String> itemDescList;
    private List<Integer> itemQtyList;

    public ItemAdapter(List<String> itemDescList, List<Integer> itemQtyList) {
        this.itemDescList = itemDescList;
        this.itemQtyList = itemQtyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemDesc = itemDescList.get(position);
        Integer itemQty = itemQtyList.get(position);
        holder.itemDesc.setText(itemDesc);
        holder.itemQty.setText(String.valueOf(itemQty));
    }

    @Override
    public int getItemCount() {
        return itemDescList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemDesc;
        public TextView itemQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDesc = itemView.findViewById(R.id.itemDesc);
            itemQty = itemView.findViewById(R.id.itemQty);
        }
    }
}
