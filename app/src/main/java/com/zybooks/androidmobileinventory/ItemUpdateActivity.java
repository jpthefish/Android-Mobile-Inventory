package com.zybooks.androidmobileinventory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ItemUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_update);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public void onSubmitButtonClick(View view) {
        // Extract user input
        EditText nameField = findViewById(R.id.item_name);
        EditText quantityField = findViewById(R.id.quantity);
        String name = nameField.getText().toString();
        int quantity = Integer.parseInt(quantityField.getText().toString());
        InventoryItem item = new InventoryItem(-1, name, quantity);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        boolean itemExists = db.itemExists(item);

        // Update item if user input matches an existing item's name, otherwise add a new item
        if (itemExists) {
            db.updateInventoryItem(item);
        } else {
            db.addInventoryItem(item);
        }

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}