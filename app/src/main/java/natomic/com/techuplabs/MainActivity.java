package natomic.com.techuplabs;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import natomic.com.techuplabs.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private myadapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<model> modelList;
    private String data;
    private ImageView picktime;
    private Button pick;
    private ActivityMainBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    int t2Hour, t2Minute;
    private int clickedPosition = -1;

    private static final int WORD_COUNT_THRESHOLD = 0;

    private AlertDialog dialog;
    private AlertDialog timePickerDialog;
    private SearchView searchView;
    private ImageView toggleView;
    private boolean isSingleColumn = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set window flags to wake up the screen and show the activity when locked
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);




        if (getIntent().getBooleanExtra("open_bottom_sheet", false)) {
            // Open the bottom sheet
            showBottomSheet();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toggleView = findViewById(R.id.imageViewLeft1);

        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSingleColumn) {
                    mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
                isSingleColumn = !isSingleColumn;
            }
        });



       searchView=findViewById(R.id.searchnotes);
       searchView.clearFocus();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               mAdapter.getFilter().filter(newText);
               return true;
           }
       });





        Button button = binding.btn;
        setContentView(binding.getRoot());
// Inside your activity or fragment
        NotificationUtils.showNotificationPermissionDialogIfNeeded(this);


        calendar = Calendar.getInstance();

        createNotificationChannel();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i=new Intent(MainActivity.this,InsertData.class);
                //startActivity(i);
                //  overridePendingTransition(R.anim.slidein, R.anim.slideout);
                showBottomSheet();
                //finish();
            }
        });
        binding.imageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,google_login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slidein1, R.anim.static_animation);
               // Toast.makeText(MainActivity.this, "image clicked", Toast.LENGTH_SHORT).show();
            }


        });
       // overridePendingTransition(R.anim.slidein1, R.anim.slideout1);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        modelList = new ArrayList<>();
        DbManager dbManager = new DbManager(this);
        Cursor cursor = dbManager.readalldata();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                model model = new model();
                model.setName(cursor.getString(1));
                model.setCreatedAt(cursor.getString(2));
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        mAdapter = new myadapter(modelList, this);
        mRecyclerView.setAdapter(mAdapter);
        new SwipeHelper(this, mRecyclerView, false) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                // Delete button

                int buttonWidth = viewHolder.itemView.getWidth() / 4; // Adjust as needed
                int buttonHeight = viewHolder.itemView.getHeight();
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_delete), // Replace with your delete icon
                        Color.parseColor("#100F0D"), // Background color
                        Color.parseColor("#fcfcfc"), // Text color
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // Handle delete action
                                // Assuming you have a SQLiteOpenHelper subclass named DbManager to manage your database
                                DbManager dbHelper = new DbManager(MainActivity.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

                                // Assuming modelList contains objects representing data in your database
                                model modelToDelete = modelList.get(pos);

                                // Define the selection criteria for deletion (assuming 'name' is the column name)
                                String selection = "name = ?";
                                String[] selectionArgs = {modelToDelete.getName()};

                                // Perform the delete operation
                                int deletedRows = db.delete("data", selection, selectionArgs);

                                // Close the database connection
                                db.close();

                                // If deletion was successful, remove the item from the list and notify the adapter
                                if (deletedRows > 0) {
                                    modelList.remove(pos);
                                    mAdapter.notifyItemRemoved(pos);
                                } else {
                                    // If deletion failed, show a toast or handle the error accordingly
                                    Toast.makeText(MainActivity.this, "Failed to delete record!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, buttonWidth, buttonHeight
                ));

                // Edit button
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_edit1), // Replace with your edit icon
                        Color.parseColor("#D9D9D9"), // Background color
                        Color.parseColor("#100F0D"), // Text color
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // Handle edit action
                                // You may open an edit activity here
                                clickedPosition = pos;
                                // Open the bottom sheet for editing the model
                                showBottomSheet1();
                            }
                        }, buttonHeight, buttonWidth
                ));
            }
        };

        binding.imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager != null) {
                    timePicker = new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setHour(12)
                            .setMinute(0)
                            .setTitleText("Set a Reminder")
                            .build();

                    timePicker.show(fragmentManager, "androidknowledge");
                    timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (timePicker != null) {
                                    int hour = timePicker.getHour();
                                    int minute = timePicker.getMinute();

                                    if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                                        // Handle the selected time here
                                        calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                                        calendar.set(Calendar.MINUTE, minute);
                                        calendar.set(Calendar.SECOND, 0);

                                        // Set the alarm for the selected time
                                        setAlarmForSelectedTime(calendar.getTimeInMillis());
                                    } else {
                                        // Handle the case of an invalid time
                                        // Show error message or handle it accordingly
                                    }
                                } else {
                                    Log.e("MainActivity", "timePicker is null");
                                    Toast.makeText(getApplicationContext(), "timepicker is null", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NullPointerException e) {
                                Log.e("MainActivity", "NullPointerException: " + e.getMessage());
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "nullpointer exception", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("MainActivity", "Exception: " + e.getMessage());
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "exception" + e.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.e("MainActivity", "FragmentManager is null");
                    Toast.makeText(MainActivity.this, "fragmentmanager is null", Toast.LENGTH_SHORT).show();
                }
            }*/
                showDialog();
            }
        });
        // Add the swipe touch listener
        View rootView = findViewById(R.id.root);
       // @SuppressLint("CutPasteId") View rootView1 = findViewById(R.id.recyclerView);
        rootView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                // Handle swipe left
            }

            @Override
            public void onSwipeRight() {

              //  finish();
              //  overridePendingTransition(R.anim.slidein1, R.anim.static_animation);
                // Create an Intent to start the new Activity
                Intent intent = new Intent(MainActivity.this, google_login.class);

                // Create the transition animation - the code below assumes a slide animation
                ActivityOptions options = ActivityOptions.makeCustomAnimation(
                        MainActivity.this, R.anim.slidein1, R.anim.static_animation
                );

                // Start the new Activity with the animation
                startActivity(intent, options.toBundle());
            }
        });
      /*  rootView1.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                // Handle swipe left
            }

            @Override
            public void onSwipeRight() {

                //  finish();
                //  overridePendingTransition(R.anim.slidein1, R.anim.static_animation);
                // Create an Intent to start the new Activity
                Intent intent = new Intent(MainActivity.this, google_login.class);

                // Create the transition animation - the code below assumes a slide animation
                ActivityOptions options = ActivityOptions.makeCustomAnimation(
                        MainActivity.this, R.anim.slidein1, R.anim.static_animation
                );

                // Start the new Activity with the animation
                startActivity(intent, options.toBundle());
            }
        });*/
    }




    public void startdbapp(View view) {
        // Your code to start the database app goes here
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarmForSelectedTime(long alarmTimeMillis) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
        long currentTimeMillis = System.currentTimeMillis();

        if (alarmTimeMillis <= currentTimeMillis) {
            // If the selected time has already passed for today, set it for the next day
            alarmTimeMillis += AlarmManager.INTERVAL_DAY;
        }
        // Set the alarm using AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);

        // Inform the user that the alarm is set
        Toast.makeText(MainActivity.this, "Reminder is set", Toast.LENGTH_SHORT).show();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMillis, AlarmManager.INTERVAL_DAY, pendingIntent);

        // Set the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Use setExactAndAllowWhileIdle for better accuracy on Android M and later
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }

        // Inform the user that the alarm is set
        // Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
    }

    private void showBottomSheet() {
        // Inflate the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_insert_data, null);

        Button button2 = bottomSheetView.findViewById(R.id.button2);
        EditText editText = bottomSheetView.findViewById(R.id.t1);
        TextView wordCountTextView = bottomSheetView.findViewById(R.id.charCount);
        ImageView i = bottomSheetView.findViewById(R.id.imageView2);



        // Create a BottomSheetDialog and set the inflated view
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Expand the bottom sheet programmatically
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // Request focus on the EditText to open the keyboard automatically
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable or disable the button based on word count
                int wordCount = countWords(s.toString());
                wordCountTextView.setText(wordCount + "/250");

                // Enable or disable the button based on word count and text length
                button2.setEnabled(wordCount > 0 && !TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputData = editText.getText().toString(); // Get the text from the EditText
                    // If no valid position is stored, add a new record
                    addRecord(inputData);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // Set up any interactions with the bottom sheet elements here (e.g., button clicks)

        // Show the bottom sheet
        bottomSheetDialog.show();
    }


    private int countWords(String text) {
        // Count all characters in the text
        return text.length();
    }
    private void addRecord(String inputData) {
        // Get the current date and time
        String currentDateTime = getCurrentDateTime();

        // Assuming you have a SQLiteOpenHelper subclass named DbManager to manage your database
        DbManager dbHelper = new DbManager(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", inputData); // Insert the input data
        values.put("created_at", currentDateTime); // Insert the current date and time

        // Insert the data into the database
        long newRowId = db.insert("data", null, values); // Replace "table_name" with the actual table name in your database

        // Check if the insertion was successful
        if (newRowId != -1) {
            // Data inserted successfully
          //  Toast.makeText(this, "Record added successfully!", Toast.LENGTH_SHORT).show();

            // Update the dataset in the adapter
            modelList.add(0, new model(inputData, currentDateTime)); // Insert at the beginning of the list with the current date and time
            mAdapter.notifyDataSetChanged(); // Notify the adapter that the dataset has changed

        } else {
            // Failed to insert data
         //   Toast.makeText(this, "Failed to add record!", Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();
    }


    private void updateRecord(model modelToUpdate, String newData) {
        // Get the current date and time
        String currentDateTime = getCurrentDateTime();

        // Update the record in the database
        DbManager dbHelper = new DbManager(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", newData); // Update the name column with the new data
        values.put("created_at", currentDateTime); // Update the creation date and time

        // Define the WHERE clause to update the specific record
        String selection = "name = ?";
        String[] selectionArgs = { modelToUpdate.getName() };

        // Perform the update operation
        int rowsAffected = db.update("data", values, selection, selectionArgs);

        // Close the database connection
        db.close();

        // Check if the update was successful
        if (rowsAffected > 0) {
            // If update was successful, update the model object and notify the adapter
            modelToUpdate.setName(newData);
            modelToUpdate.setCreatedAt(currentDateTime); // Update the model with the new creation date and time
            mAdapter.notifyItemChanged(clickedPosition);
           // Toast.makeText(this, "Record updated successfully!", Toast.LENGTH_SHORT).show();
            clickedPosition=-1;
        } else {
            // If update failed, show a toast
           // Toast.makeText(this, "Failed to update record!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDateTime() {
        // Get current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa dd-MM-yyyy ", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showBottomSheet1() {
        // Inflate the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_insert_data, null);

        Button button2 = bottomSheetView.findViewById(R.id.button2);
        EditText editText = bottomSheetView.findViewById(R.id.t1);
        TextView wordCountTextView = bottomSheetView.findViewById(R.id.charCount);
        ImageView i = bottomSheetView.findViewById(R.id.imageView2);

        if (clickedPosition != -1) {
            // Retrieve the model object corresponding to the stored position
            model modelToEdit = modelList.get(clickedPosition);
            // Your existing code to show the bottom sheet
            // Use the modelToEdit object to populate the bottom sheet fields
            editText.setText(modelToEdit.getName());
            // Continue with other fields as needed
        }

        // Initialize character count
        int initialCharacterCount = editText.getText().toString().length();
        wordCountTextView.setText(initialCharacterCount + "/250");

        // Create a BottomSheetDialog and set the inflated view
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Expand the bottom sheet programmatically
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // Request focus on the EditText to open the keyboard automatically
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable or disable the button based on word count
                int wordCount = countWords(s.toString());
                wordCountTextView.setText(wordCount + "/250");

                // Enable or disable the button based on word count and text length
                button2.setEnabled(wordCount > 0 && !TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputData = editText.getText().toString(); // Get the text from the EditText
                if (clickedPosition != -1) {
                    // If a valid position is stored, update the existing record
                    model modelToEdit = modelList.get(clickedPosition);
                    updateRecord(modelToEdit, inputData);
                } else {
                    // If no valid position is stored, add a new record
                    addRecord(inputData);
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        // Set up any interactions with the bottom sheet elements here (e.g., button clicks)

        // Show the bottom sheet
        bottomSheetDialog.show();
    }

    // Declare dialog as a class-level variable


    private void showDialog() {
        // Inflate custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.reminderdialog, null);

        // Find views in the custom layout
        ImageView imageView = dialogView.findViewById(R.id.dialogimg);
        TextView textView = dialogView.findViewById(R.id.txt_dia);
        TextView textView1 = dialogView.findViewById(R.id.txt_dia1);
        Button positiveButton = dialogView.findViewById(R.id.btn_yes);
        Button negativeButton = dialogView.findViewById(R.id.btn_no);

        // Set image, text, and button click listeners
        //  imageView.setImageResource(R.drawable.);
        //  textView.setText("Your text here");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
                // Show the second dialog
                showDialog1();
            }
        });

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        dialog = builder.create();
        dialog.show();
    }

    private void showDialog1() {
        // Inflate custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.activity_reminder,null);

        // Find views in the custom layout
        TimePicker timePicker = dialogView.findViewById(R.id.picker);
        Button setNowButton = dialogView.findViewById(R.id.setnow);
        ImageView imageView=dialogView.findViewById(R.id.image3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 timePickerDialog.dismiss();
            }
        });
        // Set onClickListener for the "Set Now" button
        setNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;

                // Check for Android version to get hour and minute
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                // Set the calendar time
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // Set the alarm
                setAlarmForSelectedTime(calendar.getTimeInMillis());

                // Dismiss the dialog
                timePickerDialog.dismiss();
            }
        });

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Create and show the AlertDialog
        timePickerDialog = builder.create();
        timePickerDialog.show();
    }
}
