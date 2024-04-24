package com.example.new2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class share_image extends AppCompatActivity {
    private ImageView i;

    ConstraintLayout constraintLayout;
    private Button btn;
    private BitmapDrawable drawable;
    private Bitmap bitmap;
    String selectedImageUrl;
    String imageresource;
    RecyclerView recyclerView;
    int selectedImageResourceId;
    int headerHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_image);
// Retrieve the data from the intent
        String dataToShare = getIntent().getStringExtra("dataToShare");

        // Find the TextView in your layout
        TextView textView = findViewById(R.id.noteText);

        // Set the data to the TextView
        textView.setText(dataToShare);
// Calculate header height (you may need to adjust this value)
       // headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        i = findViewById(R.id.imageshare);
        btn = findViewById(R.id.share);
        recyclerView=findViewById(R.id.gridRecycleview);
      // StickyHeader headerDecoration = new StickyHeader(headerHeight); // Specify the height of your sticky header
        constraintLayout=findViewById(R.id.c1);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 2 columns grid
       // recyclerView.addItemDecoration(new StickyHeader(headerHeight)); // Add StickyHeaderDecoration

        ArrayList<Integer> dataList=new ArrayList<>(); // Change data type to store resource IDs
        dataList.add(R.drawable.image1);
        dataList.add(R.drawable.image2);
        dataList.add(R.drawable.image3);
        dataList.add(R.drawable.image4);
        dataList.add(R.drawable.image5);
        dataList.add(R.drawable.image6);
        dataList.add(R.drawable.image7);
        dataList.add(R.drawable.image8);
        dataList.add(R.drawable.image9);
        dataList.add(R.drawable.image10);
        dataList.add(R.drawable.image11);
        dataList.add(R.drawable.image12);
        dataList.add(R.drawable.image13);
        dataList.add(R.drawable.image14);
        dataList.add(R.drawable.image15);
        dataList.add(R.drawable.image16);
        dataList.add(R.drawable.image17);
        dataList.add(R.drawable.image18);
        dataList.add(R.drawable.image19);
        dataList.add(R.drawable.image20);
        dataList.add(R.drawable.image21);
        dataList.add(R.drawable.image22);
        dataList.add(R.drawable.image23);
        dataList.add(R.drawable.image24);
        dataList.add(R.drawable.image25);
        dataList.add(R.drawable.image26);
        dataList.add(R.drawable.image27);
        dataList.add(R.drawable.image28);
        dataList.add(R.drawable.image29);
        dataList.add(R.drawable.image30);
        dataList.add(R.drawable.image31);
        dataList.add(R.drawable.image32);
        dataList.add(R.drawable.image33);
        dataList.add(R.drawable.image34);
        dataList.add(R.drawable.image35);
        dataList.add(R.drawable.image36);
        dataList.add(R.drawable.i1);
        dataList.add(R.drawable.i2);
        dataList.add(R.drawable.i3);
        dataList.add(R.drawable.i4);
        dataList.add(R.drawable.i5);
        dataList.add(R.drawable.i6);
        dataList.add(R.drawable.i7);
        dataList.add(R.drawable.i8);
        dataList.add(R.drawable.i9);
        dataList.add(R.drawable.i10);
        dataList.add(R.drawable.i11);
        dataList.add(R.drawable.i12);
        dataList.add(R.drawable.i13);
        dataList.add(R.drawable.i14);
        dataList.add(R.drawable.i15);
        dataList.add(R.drawable.i16);
        dataList.add(R.drawable.i17);
        dataList.add(R.drawable.i18);
        dataList.add(R.drawable.i19);
        dataList.add(R.drawable.i20);
        dataList.add(R.drawable.p1);
        dataList.add(R.drawable.p2);
        dataList.add(R.drawable.p3);
        dataList.add(R.drawable.p4);
        dataList.add(R.drawable.p5);
        dataList.add(R.drawable.p6);
        dataList.add(R.drawable.p7);
        dataList.add(R.drawable.p8);
        dataList.add(R.drawable.p9);
        dataList.add(R.drawable.p10);
        dataList.add(R.drawable.p12);
        dataList.add(R.drawable.p13);
        dataList.add(R.drawable.p14);
        dataList.add(R.drawable.p15);
        dataList.add(R.drawable.p16);
        dataList.add(R.drawable.p17);
        dataList.add(R.drawable.p18);
        dataList.add(R.drawable.p19);
        dataList.add(R.drawable.p20);
        dataList.add(R.drawable.p21);
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(dataList,this);
        // Set a click listener for RecyclerView items

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int imageResourceId) {
                selectedImageResourceId = imageResourceId; // Store selected image resource ID
                i.setImageResource(selectedImageResourceId); // Set ImageView with the selected image
                updateTextColor(); // Update text color based on image brightness
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable= (BitmapDrawable) i.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                shareImageandText(ScreenshotUtils.takeScreenshot(constraintLayout));

            }
        });
    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri=getImagetoshare(bitmap);
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        //intent.putExtra(Intent.EXTRA_TEXT,"Image text");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Image subject");
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent,"share via"));
    }

    private Uri getImagetoshare(Bitmap bitmap) {

        File folder=new File(getCacheDir(),"images");
        Uri uri=null;

        try {
            folder.mkdirs();
            File file=new File(folder,"image.jpg");
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            uri=FileProvider.getUriForFile(this,"com.example.new2",file);

        }catch (Exception e){
            e.printStackTrace();
        }
        return uri;
    }
    private double calculateBrightness(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixelsCount = width * height;
        int[] pixels = new int[pixelsCount];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        long sum = 0;
        for (int color : pixels) {
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            // Calculate brightness using the formula (0.299 * R + 0.587 * G + 0.114 * B)
            sum += 0.299 * r + 0.587 * g + 0.114 * b;
        }

        // Calculate the average brightness
        return sum / pixelsCount;
    }
    private void updateTextColor() {
        Drawable backgroundDrawable = i.getDrawable();
        Bitmap backgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
        double brightness = calculateBrightness(backgroundBitmap);
        TextView textView = findViewById(R.id.noteText);
        if (brightness < 128) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.BLACK);
        }
    }
}