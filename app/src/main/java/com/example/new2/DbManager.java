package com.example.new2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DbManager extends SQLiteOpenHelper
{
    private static final String dbname="new2.db";
    private static final String TABLE_NAME = "data";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "name";



    public DbManager(Context context)
    {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String qry = "CREATE TABLE "+TABLE_NAME+" ("+
                KEY_ID+" INTEGER PRIMARY KEY,"+
                KEY_TITLE+" TEXT,"+ "created_at DATETIME DEFAULT CURRENT_TIMESTAMP"+")";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS data");
        onCreate(db);
    }

    public long addRecord(model dataToInsert, String currentDateTime)
    {
        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues cv=new ContentValues();
        cv.put("name", dataToInsert.getName());
       cv.put("created_at", dataToInsert.getCreatedAt());
        return db.insert("data",null,cv);
       // long res=db.insert("data",null,cv);

      // Log.d("inserted","res->"+res);

      // return res;

    }
    private String getCurrentDateTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa dd-MM-yyyy", Locale.getDefault());
    Date currentDate = Calendar.getInstance().getTime();
    return dateFormat.format(currentDate);
}
    public Cursor readalldata()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String qry="select * from data order by id desc";
        Cursor cursor=db.rawQuery(qry,null);
        return  db.rawQuery(qry,null);
    }
    public void deleteRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public int updateRecord(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, newName);
        return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

}

