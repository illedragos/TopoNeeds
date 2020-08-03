package com.example.toponeeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.toponeeds.model.Item;
import com.example.toponeeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOPO_TABLE = "CREATE TABLE "+Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TOPO_ITEM + " TEXT,"
                + Constants.KEY_QUANTITY_NUMBER +" INTEGER,"
                + Constants.KEY_COLOR + " TEXT,"
                + Constants.KEY_ITEM_SIZE_NUMBER + " INTEGER,"
                + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_TOPO_TABLE);
        //db.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(db);
    }

    //CRUD OPERATION
    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_TOPO_ITEM,item.getItemName());
        contentValues.put(Constants.KEY_QUANTITY_NUMBER,item.getItemQuantity());
        contentValues.put(Constants.KEY_COLOR,item.getItemColor());
        contentValues.put(Constants.KEY_ITEM_SIZE_NUMBER,item.getItemSize());
        contentValues.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());//timestamp of the system

        //Insert to row
        db.insert(Constants.TABLE_NAME,null,contentValues);
        Log.d("DBHandler","Added Item::::: ");

        db.close();
    }

    //get an Item
    public Item getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_TOPO_ITEM,
                        Constants.KEY_QUANTITY_NUMBER,
                        Constants.KEY_COLOR,
                        Constants.KEY_ITEM_SIZE_NUMBER,
                        Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        if(cursor!=null){
            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_TOPO_ITEM)));
            item.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER))));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
            item.setItemSize(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE_NUMBER))));

            //convert Timestamp into something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            item.setDateItemAdded(formattedDate);
        }


        return item;
    }

    //get all items
    public List<Item> getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID,
                        Constants.KEY_TOPO_ITEM,
                        Constants.KEY_QUANTITY_NUMBER,
                        Constants.KEY_COLOR,
                        Constants.KEY_ITEM_SIZE_NUMBER,
                        Constants.KEY_DATE_NAME},
                null,null,null,null,
                Constants.KEY_DATE_NAME + " DESC");

        if(cursor.moveToFirst()){
            do{
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_TOPO_ITEM)));
                item.setItemQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER))));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
                item.setItemSize(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE_NUMBER))));

                //convert Timestamp into something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                item.setDateItemAdded(formattedDate);

                itemList.add(item);

            }while(cursor.moveToNext());
        }

        return itemList;
    }
    //Todo: Add updateItem
    //Todo: Add Delete Item
    //Todo: getItemCount

    //Add updateItem
    public int updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_TOPO_ITEM,item.getItemName());
        contentValues.put(Constants.KEY_QUANTITY_NUMBER,item.getItemQuantity());
        contentValues.put(Constants.KEY_COLOR,item.getItemColor());
        contentValues.put(Constants.KEY_ITEM_SIZE_NUMBER,item.getItemSize());
        contentValues.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());//timestamp of the system

        //update row
        return db.update(Constants.TABLE_NAME, contentValues,Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    //Delete Item
    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    //getItemCount
    public int getItemCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }


}
