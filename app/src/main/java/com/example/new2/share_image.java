package com.example.new2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.content.FileProvider;
public class share_image extends AppCompatActivity {
    private ImageView i;
private Button btn;
private BitmapDrawable drawable;
private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_image);


        i = findViewById(R.id.imageshare);
        btn = findViewById(R.id.share);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable= (BitmapDrawable) i.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                shareImageandText(bitmap);

            }
        });
    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri=getImagetoshare(bitmap);
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT,"Image text");
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


}