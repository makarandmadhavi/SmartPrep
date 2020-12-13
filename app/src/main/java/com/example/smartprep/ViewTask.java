package com.example.smartprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class ViewTask extends AppCompatActivity {

    TextView taskview;
    String task;
    String project_id;
    String id;
    String status;
    TextView mDateTime;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String timestamp;
    TextView showLocation;
    LocationManager locationManager;
    double latitude;
    double longitude;
    ImageView taskpic;
    byte[] img;
    Bitmap bmp =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Intent intent = getIntent();
        taskview = findViewById(R.id.tasktext);
        taskview.setText(intent.getStringExtra("task"));
        project_id = intent.getStringExtra("projectid");
        id = intent.getStringExtra("id");
        status = intent.getStringExtra("status");
        mDateTime = findViewById(R.id.datetime);
        mDateTime.setText("Reminder: "+intent.getStringExtra(DBHelper.TASK_TIMESTAMP));
        showLocation = findViewById(R.id.showLocation);
        showLocation.setText(intent.getStringExtra(DBHelper.TASK_LOCATION));
        taskpic = findViewById(R.id.taskpic);
        DBHelper db = new DBHelper(this);
        img = db.taskImage(id);
        taskpic.setImageBitmap(getImage(img));
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        try{
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (Exception e){
            return null;
        }

    }

    public void openImage(View view) {
        Intent intent = new Intent(ViewTask.this,Viewimage.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void openLocation(View view) {
        GPSTracker gpstracker = new GPSTracker(this);
        latitude = gpstracker.getLatitude();
        longitude = gpstracker.getLongitude();
        Uri gmmIntentUri = Uri.parse("geo:19.2163789,72.9618969" + "?q=" + Uri.encode(showLocation.getText().toString()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}