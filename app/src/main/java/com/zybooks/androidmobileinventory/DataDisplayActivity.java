package com.zybooks.androidmobileinventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.*;

public class DataDisplayActivity extends AppCompatActivity {
    DatabaseHelper db;
    InventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate database
        db = new DatabaseHelper(getApplicationContext());

        // Populate RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new InventoryAdapter(db, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notifications) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);

            //for Android 5-7
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);

            // for Android 8 and above
            intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());

            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // Update RecyclerView after add/update functions
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Check that it's the result from ItemUpdateActivity
            if (resultCode == RESULT_OK) {
                // Refresh the RecyclerView
                List<InventoryItem> inventoryItems = db.getAllInventoryItems();
                adapter.updateData(inventoryItems);
            }
        }
    }

    public void onAddItemClick(View view) {
        Intent intent = new Intent(this, ItemUpdateActivity.class);
        startActivityForResult(intent, 1);
    }

    public void onUpdateItemClick(View view) {
        Intent intent = new Intent(this, ItemUpdateActivity.class);
        startActivityForResult(intent, 1);
    }

}