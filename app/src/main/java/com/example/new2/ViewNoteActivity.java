package com.example.new2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.MenuCompat;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notes);

        // Get the data passed from MainActivity
        String noteName = getIntent().getStringExtra("name");
        String noteName1 = getIntent().getStringExtra("createdDate");
        // Set the note name to the TextView
        TextView noteTextView = findViewById(R.id.view);
        noteTextView.setText(noteName);
        TextView noteTextView1 = findViewById(R.id.view1);
        noteTextView1.setText(noteName1);
        ImageView imageView = findViewById(R.id.backbutton);
        ImageView menuImageView = findViewById(R.id.menu);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set click listener to the ImageView
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v); // Pass 'v' directly
        popupMenu.inflate(R.menu.popup_menu); // Inflate your menu resource
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.s) {
                    // Handle share action
                    showDialog();
                    return true;
                } else if (itemId == R.id.e) {
                    // Handle edit action
                    return true;
                } else if (itemId == R.id.d) {
                    // Handle delete action
                    return true;
                }
                return false;
            }
        });

        // Show icons in the popup menu
        MenuCompat.setGroupDividerEnabled(popupMenu.getMenu(), true);

        // Creating MenuPopupHelper
        @SuppressLint("RestrictedApi")
        MenuPopupHelper menuHelper = new MenuPopupHelper(ViewNoteActivity.this,
                (MenuBuilder) popupMenu.getMenu(), v);
        menuHelper.setForceShowIcon(true); // Set to true to show icons
        menuHelper.show();
    }
    private void showDialog() {
        // Inflate custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);

        // Find views in the custom layout
        ImageView imageView = dialogView.findViewById(R.id.dialogimg);
        TextView textView = dialogView.findViewById(R.id.txt_dia);
        Button positiveButton = dialogView.findViewById(R.id.btn_yes);
        Button negativeButton = dialogView.findViewById(R.id.btn_no);

        // Set image, text, and button click listeners
      //  imageView.setImageResource(R.drawable.);
      //  textView.setText("Your text here");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from intent
                String dataToShare = getIntent().getStringExtra("name");

                // Create the Intent for sharing
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, dataToShare);
                intent.setType("text/plain");

                // Verify that there's an activity to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when Cancel button is clicked
                Intent intent = new Intent(ViewNoteActivity.this, share_image.class); // Replace CurrentActivity with the name of your current activity and OtherActivity with the name of the activity you want to start
                startActivity(intent);

            }
        });

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
