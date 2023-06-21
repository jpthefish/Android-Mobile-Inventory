package com.zybooks.androidmobileinventory;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryItemViewHolder> {
    // Instantiate database
    DatabaseHelper db;
    List<InventoryItem> inventoryItems;
    Context context;

    public InventoryAdapter(DatabaseHelper db, Context context) {
        this.db = db;
        this.inventoryItems = db.getAllInventoryItems();
        this.context = context;
    }

    @NonNull
    @Override
    public InventoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new InventoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryItemViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);

        // Attempt to send SMS notification if item's inventory is low
        if (item.getQuantity() == 0) sendLowInventoryNotification(item.getName());

        // Populate each item in the view
        holder.textViewId.setText(String.valueOf(item.getId()));
        holder.textViewName.setText(item.getName());
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));

        holder.buttonDelete.setOnClickListener(v -> {
            // Delete item at position from database
            db.deleteInventoryItem(item);

            // Remove item from data list and notify adapter
            inventoryItems.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    // Refresh RecyclerView with new data
    public void updateData(List<InventoryItem> newInventoryItems) {
        this.inventoryItems = newInventoryItems;
        notifyDataSetChanged();
    }

    // Sends SMS notification for a given item if permissions are granted
    public void sendLowInventoryNotification(String itemName) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            // Send message to Android emulator phone number
            smsManager.sendTextMessage("5555215554", null, "Low inventory for item: " + itemName, null, null);
        }
    }

}


