package natomic.com.techuplabs;

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
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class share_image extends AppCompatActivity implements InnerRecyclerViewAdapter.OnItemClickListener {
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

        ImageView imageView=findViewById(R.id.backbutton1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.notesin,R.anim.notesout);
            }
        });

        // Set the data to the TextView
        textView.setText(dataToShare);
// Calculate header height (you may need to adjust this value)
       // headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        i = findViewById(R.id.imageshare);
        btn = findViewById(R.id.share);
        recyclerView=findViewById(R.id.gridRecycleview);
      // StickyHeader headerDecoration = new StickyHeader(headerHeight); // Specify the height of your sticky header
        constraintLayout=findViewById(R.id.c1);

       // recyclerView.addItemDecoration(new StickyHeader(headerHeight)); // Add StickyHeaderDecoration

     /*   ArrayList<Integer> dataList=new ArrayList<>(); // Change data type to store resource IDs
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
        dataList.add(R.drawable.p21);*/
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4)); // 2 columns grid
       // RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(dataList,this);
        // Set a click listener for RecyclerView items
      //  recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OuterRecyclerViewAdapter outerAdapter = new OuterRecyclerViewAdapter(this, generateOuterItems());
        recyclerView.setAdapter(outerAdapter);
        outerAdapter.setOnItemClickListener(new OuterRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int imageResourceId) {
                // Handle item click here
                i.setImageResource(imageResourceId);
                updateTextColor(); // Update text color based on image brightness
            }
        });

      /*  recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int imageResourceId) {
                selectedImageResourceId = imageResourceId; // Store selected image resource ID
                i.setImageResource(selectedImageResourceId); // Set ImageView with the selected image
                updateTextColor(); // Update text color based on image brightness
            }
        });*/




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable= (BitmapDrawable) i.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                shareImageandText(ScreenshotUtils.takeScreenshot(constraintLayout));

            }
        });

    }
    private List<OuterItem> generateOuterItems() {
        List<OuterItem> outerItems = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.image1);
            innerImages.add(R.drawable.image2);
            innerImages.add(R.drawable.image3);
            innerImages.add(R.drawable.image4);
            innerImages.add(R.drawable.image5);
            innerImages.add(R.drawable.image6);
            innerImages.add(R.drawable.image7);
            innerImages.add(R.drawable.image8);
            innerImages.add(R.drawable.image9);
            innerImages.add(R.drawable.image10);
            innerImages.add(R.drawable.image11);
            innerImages.add(R.drawable.image12);
            innerImages.add(R.drawable.image13);
            innerImages.add(R.drawable.image14);
            innerImages.add(R.drawable.image15);
            innerImages.add(R.drawable.image16);
            innerImages.add(R.drawable.image17);
            innerImages.add(R.drawable.image18);
            innerImages.add(R.drawable.image19);
            innerImages.add(R.drawable.image20);
            outerItems.add(new OuterItem("Static colors ", innerImages));
        }
        List<OuterItem> outerItems1 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.image21);
            innerImages.add(R.drawable.image22);
            innerImages.add(R.drawable.image23);
            innerImages.add(R.drawable.image24);
            innerImages.add(R.drawable.image25);
            innerImages.add(R.drawable.image26);
            innerImages.add(R.drawable.image27);
            innerImages.add(R.drawable.image28);
            innerImages.add(R.drawable.image29);
            innerImages.add(R.drawable.image30);
            innerImages.add(R.drawable.image31);
            innerImages.add(R.drawable.image32);
            innerImages.add(R.drawable.image33);
            innerImages.add(R.drawable.image34);
            innerImages.add(R.drawable.image35);
            innerImages.add(R.drawable.image36);
            outerItems.add(new OuterItem("Gradient " , innerImages));
        }
        List<OuterItem> outerItems2 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.i1);
            innerImages.add(R.drawable.i2);
            innerImages.add(R.drawable.i3);
            innerImages.add(R.drawable.i4);
            innerImages.add(R.drawable.i5);
            innerImages.add(R.drawable.i6);
            innerImages.add(R.drawable.i7);
            innerImages.add(R.drawable.i8);
            innerImages.add(R.drawable.i9);
            innerImages.add(R.drawable.i10);
            innerImages.add(R.drawable.i11);
            innerImages.add(R.drawable.i12);
            outerItems.add(new OuterItem("Abstract " , innerImages));
        }
        List<OuterItem> outerItems3 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.i13);
            innerImages.add(R.drawable.i14);
            innerImages.add(R.drawable.i15);
            innerImages.add(R.drawable.i16);
            innerImages.add(R.drawable.i17);
            innerImages.add(R.drawable.i18);
            innerImages.add(R.drawable.i19);
            innerImages.add(R.drawable.i20);
            outerItems.add(new OuterItem("Celebration " , innerImages));
        }
        List<OuterItem> outerItems4 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.p1);
            innerImages.add(R.drawable.p2);
            innerImages.add(R.drawable.p3);
            innerImages.add(R.drawable.p4);
            innerImages.add(R.drawable.p5);
            innerImages.add(R.drawable.p6);
            innerImages.add(R.drawable.p7);
            innerImages.add(R.drawable.p8);
            innerImages.add(R.drawable.p9);
            innerImages.add(R.drawable.p10);
            innerImages.add(R.drawable.p12);
            innerImages.add(R.drawable.p13);
            outerItems.add(new OuterItem("Zen " , innerImages));
        }
        List<OuterItem> outerItems5 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.p14);
            innerImages.add(R.drawable.p15);
            innerImages.add(R.drawable.p16);
            innerImages.add(R.drawable.p17);

            outerItems.add(new OuterItem("Travel " , innerImages));
        }
        List<OuterItem> outerItems6 = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            List<Integer> innerImages = new ArrayList<>();
            innerImages.add(R.drawable.p18);
            innerImages.add(R.drawable.p19);
            innerImages.add(R.drawable.p20);
            innerImages.add(R.drawable.p21);

            outerItems.add(new OuterItem("Notebook " , innerImages));
        }
        return outerItems;
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

    @Override
    public void onItemClick(int imageResourceId) {
        // Update ImageView with the selected image
       // Log.d("ItemClick", "Clicked on item with resource ID: " );

        i.setImageResource(imageResourceId);
        updateTextColor(); // Update text color based on image brightness
    }


}