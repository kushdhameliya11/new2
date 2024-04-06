package com.example.new2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertData extends AppCompatActivity {
    TextView wordCountTextView;
    EditText t1;
    Button button2;
    TextView charCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        button2=findViewById(R.id.button2);

        Intent intent = getIntent();
        if (intent != null) {
            String receivedData = intent.getStringExtra("key");

            // Now you have the data in the 'receivedData' variable
            // You can use it as needed

            // For example, if you want to set the received data in the EditText 't1'
            t1 = findViewById(R.id.t1);
            t1.setText(receivedData);


        }

      //  t1=(EditText) findViewById(R.id.t1);
        Button post = (Button) findViewById(R.id.button2);
        post.setEnabled(false);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(view);
            }

        });
        charCountTextView = findViewById(R.id.charCount);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userInput = s.toString();
                int charCount = userInput.length();
                charCountTextView.setText(charCount + "/250");
                post.setEnabled(!userInput.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }
    public void addRecord(View view){
        DbManager db=new DbManager(this);
        String userInput = t1.getText().toString();
        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter non-empty data", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] words = userInput.split(" ");
        int charCount = userInput.length();

        String currentDateTime = getCurrentDateTime();

        // Concatenate the user input and current date/time
        model dataToInsert = new model(userInput, currentDateTime);

        long res = db.addRecord(dataToInsert,currentDateTime);
        String toastMessage = (res != -1) ? "Record inserted successfully" : "Failed to insert record";
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

        t1.setText("");

        button2 =findViewById(R.id.button2);
        Intent i=new Intent(InsertData.this,MainActivity.class);
        startActivity(i);

    }
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        return dateFormat.format(currentDate);
    }



}