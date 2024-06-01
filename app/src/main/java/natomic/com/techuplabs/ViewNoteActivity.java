package natomic.com.techuplabs;

import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class ViewNoteActivity extends AppCompatActivity {

    private AlertDialog dialog; // Declare dialog as a field of the class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notes);


        View rootView = findViewById(R.id.viewnoteroot); // Make sure your root layout has this ID

        rootView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {

            }

            @Override
            public void onSwipeRight() {
                finish();
                overridePendingTransition(R.anim.static_animation, R.anim.notesout);
            }
        });



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
                overridePendingTransition(R.anim.static_animation,R.anim.notesout);
               // Toast.makeText(ViewNoteActivity.this, "back image click", Toast.LENGTH_SHORT).show();

            }
        });
        // Set click listener to the ImageView
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

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
        // imageView.setImageResource(R.drawable.);
        // textView.setText("Your text here");
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

                // Dismiss the dialog
                dismissDialog();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when Cancel button is clicked
                Intent intent = new Intent(ViewNoteActivity.this, share_image.class);
                intent.putExtra("dataToShare", getIntent().getStringExtra("name"));
                startActivity(intent);

                // Dismiss the dialog
                dismissDialog();
            }
        });

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        dialog = builder.create();
        dialog.show();
    }
    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Apply the animation
        overridePendingTransition(R.anim.static_animation,R.anim.notesout);
        // Call the super method to handle the back button press

    }


}
