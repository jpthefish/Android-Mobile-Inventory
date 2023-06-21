package com.zybooks.androidmobileinventory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Prompts user for SMS notification permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
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

    public void onLoginClick(View view) {
        // Extract user input
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        boolean userExists = db.userExists(new User(username, password));

        if (!userExists) {
            // Alert user that information is invalid
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("Invalid username and/or password");

            builder.setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Display main menu
            Intent intent = new Intent(this, DataDisplayActivity.class);
            startActivity(intent);
        }
    }

    public void onRegisterClick(View view) {
        // Extract user input
        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        boolean userExists = db.userExists(new User(username, password));

        if (userExists) {
            // Alert user that account already exists
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Alert");
            builder.setMessage("User already exists");

            builder.setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Register a new user
            db.addUser(new User(username, password));

            // Display main menu
            Intent intent = new Intent(this, DataDisplayActivity.class);
            startActivity(intent);
        }
    }

}