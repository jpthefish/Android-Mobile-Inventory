package com.zybooks.androidmobileinventory;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryItemViewHolder extends RecyclerView.ViewHolder {
    TextView textViewId;
    TextView textViewName;
    TextView textViewQuantity;
    Button buttonDelete;

    public InventoryItemViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewId = itemView.findViewById(R.id.text_view_id);
        textViewName = itemView.findViewById(R.id.text_view_name);
        textViewQuantity = itemView.findViewById(R.id.text_view_quantity);
        buttonDelete = itemView.findViewById(R.id.button_delete);
    }
}
