package com.example.new2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuview,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notes);

        // Get the data passed from MainActivity
        String noteName = getIntent().getStringExtra("name");
        String noteName1=getIntent().getStringExtra("createdDate");
        // Set the note name to the TextView
        TextView noteTextView = findViewById(R.id.view);
        noteTextView.setText(noteName);
        TextView noteTextView1 = findViewById(R.id.view1);
        noteTextView1.setText(noteName1);
        ImageView closeImageView = findViewById(R.id.backbutton);

        // Set click listener to the ImageView
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity
                finish();
                //overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse);
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        // Apply slide-out animation when finishing the activity
        overridePendingTransition(R.anim.slide_in_reverse, R.anim.slide_out_reverse);
    }
}

