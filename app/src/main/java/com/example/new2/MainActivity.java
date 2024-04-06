package com.example.new2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.example.new2.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.Gravity;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<model> modelList;

    private ImageView picktime;
    private Button pick;
    private ActivityMainBinding binding;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    int t2Hour, t2Minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button button = binding.btn;

       // setContentView(binding.getRoot());

        createNotificationChannel();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Intent i=new Intent(MainActivity.this,InsertData.class);
             // startActivity(i);

                showBottomSheet();
            }
        });


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

        mAdapter = new myadapter(modelList);
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
                                modelList.remove(pos);
                                mAdapter.notifyItemRemoved(pos);
                            }
                        },buttonWidth,buttonHeight
                ));

                // Edit button
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_edit), // Replace with your edit icon
                        Color.parseColor("#D9D9D9"), // Background color
                        Color.parseColor("#100F0D"), // Text color
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // Handle edit action
                                // You may open an edit activity here
                            }
                        },buttonHeight,buttonWidth
                ));
            }
        };
        binding.imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Set a Reminder")
                    .build();

            timePicker.show(getSupportFragmentManager(), "androidknowledge");
            timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (timePicker.getHour() > 12){
                        //binding.textView3.setText(
                           //     String.format("%02d",(timePicker.getHour()-12)) +":"+ String.format("%02d", timePicker.getMinute())+"PM"
                       // );
                    } else  {
                       // binding.textView3.setText(timePicker.getHour()+":" + timePicker.getMinute()+ "AM");
                    }

                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    calendar.set(Calendar.SECOND, 0);
                   // calendar.set(Calendar.MILLISECOND, 0);
                    setAlarmForSelectedTime(calendar.getTimeInMillis());
                    //Toast.makeText(MainActivity.this,"Alarm is set for " + binding.textView3.getText(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    });

            binding.set.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calendar != null) {
                // Set the alarm for the selected time
                setAlarmForSelectedTime(calendar.getTimeInMillis());
                Toast.makeText(MainActivity.this,"reminder is set",Toast.LENGTH_SHORT).show();
            } else {
                // Inform the user that a valid time needs to be selected
                Toast.makeText(MainActivity.this, "Please select a valid time", Toast.LENGTH_SHORT).show();
            }
        }
    });

        /*    binding.cansel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager == null){
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);
           // Toast.makeText(MainActivity.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    });*/
    }

    public void startdbapp(View view) {
        // Your code to start the database app goes here
    }


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long currentTimeMillis = System.currentTimeMillis();

        if (alarmTimeMillis <= currentTimeMillis) {
            // If the selected time has already passed for today, set it for the next day
            alarmTimeMillis += AlarmManager.INTERVAL_DAY;
        }

        // Cancel any existing alarms
        alarmManager.cancel(pendingIntent);

        // Set the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Use setExactAndAllowWhileIdle for better accuracy on Android M and later
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }

        // Inform the user that the alarm is set
        Toast.makeText(MainActivity.this, "Reminder Set", Toast.LENGTH_SHORT).show();
    }

    private void showBottomSheet() {
        // Inflate the layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_insert_data,null);

        // Create a BottomSheetDialog and set the inflated view
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);


        // Set up any interactions with the bottom sheet elements here (e.g., button clicks)

        // Show the bottom sheet
        bottomSheetDialog.show();
    }

}