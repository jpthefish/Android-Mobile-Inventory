package com.zybooks.androidmobileinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "InventoryApp.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "ID";
    private static final String COLUMN_USER_USERNAME = "Username";
    private static final String COLUMN_USER_PASSWORD = "Password";

    private static final String TABLE_INVENTORY = "Inventory";
    private static final String COLUMN_INVENTORY_ID = "ID";
    private static final String COLUMN_INVENTORY_NAME = "Name";
    private static final String COLUMN_INVENTORY_QUANTITY = "Quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create two tables with schema specified above
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_USERNAME + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT UNIQUE" + ")";

        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + TABLE_INVENTORY + "("
                + COLUMN_INVENTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_INVENTORY_NAME + " TEXT UNIQUE,"
                + COLUMN_INVENTORY_QUANTITY + " INTEGER" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_INVENTORY_TABLE);

        // Generate default inventory data
        String ADD_DEFAULT_ITEMS = "INSERT INTO " + TABLE_INVENTORY +
                " (" + COLUMN_INVENTORY_NAME + ", " + COLUMN_INVENTORY_QUANTITY + ") " +
                "VALUES ('Pizza', 6), ('Taco', 17), ('Sandwich', 2)";
        db.execSQL(ADD_DEFAULT_ITEMS);

        // Generate default user data
        String ADD_DEFAULT_USER = "INSERT INTO " + TABLE_USERS +
                " (" + COLUMN_USER_USERNAME + ", " + COLUMN_USER_PASSWORD + ") " +
                "VALUES ('jpthefish', 'password123')";
        db.execSQL(ADD_DEFAULT_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deletes both tables and starts over
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        onCreate(db);
    }

    /* USERS TABLE CRUD METHODS */

    // Create a new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Check if a user is in the database
    public boolean userExists(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_USERNAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{user.getUsername(), user.getPassword()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    /* INVENTORY TABLE CRUD METHODS */

    // Create a new inventory item
    public void addInventoryItem(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INVENTORY_NAME, item.getName());
        values.put(COLUMN_INVENTORY_QUANTITY, item.getQuantity());

        db.insert(TABLE_INVENTORY, null, values);
        db.close();
    }

    // Read all inventory items
    public List<InventoryItem> getAllInventoryItems() {
        List<InventoryItem> inventoryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVENTORY, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_INVENTORY_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_INVENTORY_NAME);
                int quantityIndex = cursor.getColumnIndex(COLUMN_INVENTORY_QUANTITY);
                // Break if column doesn't exist
                if (idIndex == -1 || nameIndex == -1 || quantityIndex == -1) break;

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                int quantity = cursor.getInt(quantityIndex);

                inventoryItems.add(new InventoryItem(id, name, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return inventoryItems;
    }

    // Check if an item is in the database
    public boolean itemExists(InventoryItem item) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INVENTORY + " WHERE " + COLUMN_INVENTORY_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{item.getName()});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    // Update an inventory item
    public void updateInventoryItem(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INVENTORY_NAME, item.getName());
        values.put(COLUMN_INVENTORY_QUANTITY, item.getQuantity());

        db.update(TABLE_INVENTORY, values, COLUMN_INVENTORY_NAME + " = ?",
                new String[]{item.getName()});
        db.close();
    }

    // Delete an inventory item
    public void deleteInventoryItem(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_INVENTORY, COLUMN_INVENTORY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
    }

}
