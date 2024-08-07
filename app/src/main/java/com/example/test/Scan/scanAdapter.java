package com.example.test.Scan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.R;
import java.util.List;

public class scanAdapter extends RecyclerView.Adapter<scanAdapter.ViewHolder> {

    private List<String> barCodeList;
    private List<Integer> qtyList;

    public scanAdapter(List<String> barCodeList, List<Integer> qtyList) {
        this.barCodeList = barCodeList;
        this.qtyList = qtyList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scanner_data  , parent, false);
        return new scanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String barCode = barCodeList.get(position);
        Integer qty = qtyList.get(position);
        holder.barCode.setText(barCode);
        holder.qty.setText(String.valueOf(qty));
    }

    @Override
    public int getItemCount() {
        return barCodeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView barCode;
        public TextView qty;

        public ViewHolder(View itemView) {
            super(itemView);
            barCode = itemView.findViewById(R.id.barCode);
            qty = itemView.findViewById(R.id.Qty);
        }
    }
}