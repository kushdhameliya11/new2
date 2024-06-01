package natomic.com.techuplabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class login extends AppCompatActivity implements ProfileAdapter.OnItemClickListener {

    private FirebaseAuth mAuth;
    private myadapter adapter;
    private ArrayList<model> dataList;
    private RecyclerView recyclerView;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View rootView = findViewById(R.id.end); // Make sure your root layout has this ID

        rootView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                finish();
                overridePendingTransition(R.anim.static_animation, R.anim.profileout);
            }

            @Override
            public void onSwipeRight() {
                // Optionally handle swipe right action
            }
        });

        mAuth = FirebaseAuth.getInstance();

        ImageView i1 = findViewById(R.id.profilephoto);
        TextView t1 = findViewById(R.id.profiletext1);
        TextView t2 = findViewById(R.id.profiletext2);
        TextView t3 = findViewById(R.id.profiletext);
        ImageView imageView = findViewById(R.id.backbutton1);
        Button logout = findViewById(R.id.logout);
        RecyclerView profilerecycler = findViewById(R.id.profilerecycler);
        // Initialize RecyclerView
        profilerecycler.setLayoutManager(new LinearLayoutManager(this));

        // Sample data to display in RecyclerView
        int[] profileImages1 = {R.drawable.arrow1, R.drawable.arrow, R.drawable.arrow}; // Sample image resources for the second ImageView
        String[] profileData = {"Share", "Feedback", "Delete Account"}; // Sample data for TextView
        int[] profileImages = {R.drawable.shareprofile, R.drawable.feedback, R.drawable.deleteprofile}; // Sample image resources for the first ImageView

        // Create adapter with sample data
        ProfileAdapter profileAdapter = new ProfileAdapter(this, profileData, profileImages1, profileImages, this);
        profilerecycler.setAdapter(profileAdapter);
// Add the item decoration
        DeviderItemDecoration dividerItemDecoration = new DeviderItemDecoration(this);
        profilerecycler.addItemDecoration(dividerItemDecoration);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            t1.setText(account.getDisplayName());
            t2.setText(account.getEmail());
            //  Glide.with(this).load(account.getPhotoUrl()).into(i1);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
                finish();
                overridePendingTransition(R.anim.static_animation, R.anim.profileout);
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(), R.style.CustomAlertDialogTheme)
                        .setIcon(getDrawable(R.drawable.profile))
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton("No", null);

                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                // Show the AlertDialog
                dialog.show();

                // Set the button text color after showing the dialog
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Apply the animation
        overridePendingTransition(R.anim.static_animation, R.anim.profileout);
        // Call the super method to handle the back button press
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        // Clear Google sign-in account selection history
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // After signing out, redirect to google_login activity
                startActivity(new Intent(getApplicationContext(), google_login.class));
                finish(); // Finish the current activity after starting google_login activity
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Log.d("ItemClick", "Clicked item at position: " + position);
        if (position == 2) {
            // Show dialog when clicking on the second item (position 1)
            showdeleteDialog();
        }
    }

    private void showdeleteDialog() {
        // Inflate custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.deleteaccountdialog, null);

        // Find views in the custom layout
        ImageView imageView = dialogView.findViewById(R.id.dialogdelete);
        TextView t1 = dialogView.findViewById(R.id.txt_1);
        Button b1 = dialogView.findViewById(R.id.btn_yes);
        Button b2 = dialogView.findViewById(R.id.btn_no);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action
                dialog.dismiss();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get an instance of DbManager
                DbManager dbManager = new DbManager(login.this);

                // Delete all records
                dbManager.deleteAllRecords();

                // Refresh the RecyclerView
                adapter.notifyItemRemoved(pos);

                // Dismiss the dialog
                dialog.dismiss();

                // Retrieve the Activity from the Context
                Context context = v.getContext();
                while (context instanceof ContextWrapper) {
                    if (context instanceof Activity) {
                        break;
                    }
                    context = ((ContextWrapper) context).getBaseContext();
                }

                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    Log.d("Transition", "Starting MainActivity with animation");
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.static_animation, R.anim.profileout); // Move this line here

                } else {
                    Log.d("Transition", "Context is NOT an Activity");
                }
            }
        });



        dialog.show();

    }


    private void refreshRecyclerView() {
        // Assuming you have a method to get the updated data from the database
        ArrayList<model> updatedData = getUpdatedDataFromDb();

        // Assuming you have a RecyclerView adapter called 'adapter'
        adapter.setData(updatedData);
        adapter.notifyDataSetChanged();
    }

    // Method to get updated data from the database
    private ArrayList<model> getUpdatedDataFromDb() {
        DbManager dbManager = new DbManager(this);
        Cursor cursor = dbManager.readalldata();
        ArrayList<model> dataList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int createdAtIndex = cursor.getColumnIndex("created_at");

            // Ensure the column indices are valid
            if (nameIndex >= 0 && createdAtIndex >= 0) {
                do {
                    String name = cursor.getString(nameIndex);
                    String createdAt = cursor.getString(createdAtIndex);
                    model data = new model(name, createdAt);
                    dataList.add(data);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return dataList;
    }
}