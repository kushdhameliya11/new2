package com.example.new2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class fetchdata extends AppCompatActivity
{

    RecyclerView recyclerView;
    ArrayList<model> dataholder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetchdata);

        recyclerView=(RecyclerView)findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Cursor cursor=new DbManager(this).readalldata();
        dataholder=new ArrayList<>();
        int columnIndexName = cursor.getColumnIndex("name"); // Adjust this to match your column name
        int columnIndexCreatedAt = cursor.getColumnIndex("created_at");
        if (columnIndexName != -1 && columnIndexCreatedAt != -1) {
            while (cursor.moveToNext()) {
                model obj = new model(cursor.getString(columnIndexName),cursor.getString(columnIndexCreatedAt));
                obj.setCreatedAt(cursor.getString(columnIndexCreatedAt));
                dataholder.add(obj);
            }
        }else{
            Log.e("fetchdata", "One or both column indexes not found");
        }

        myadapter adapter=new myadapter(dataholder);
        recyclerView.setAdapter(adapter);

    }
}
