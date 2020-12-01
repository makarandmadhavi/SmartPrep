package com.example.smartprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    TextView mUsername;
    EditText mDisplayname;
    DBHelper db;
    HashMap<String ,String> user;
    private static final int pic_id = 123;
    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = new DBHelper(this);
        user = db.checklogin();
        mUsername = findViewById(R.id.username);
        mDisplayname = findViewById(R.id.displayname);
        profilepic = findViewById(R.id.profilepic);
        mUsername.setText(user.get("username"));
        mDisplayname.setText(user.get("name"));
        byte[] img = null;
        try {
            img = db.getDp(user.get("id"));
            profilepic.setImageBitmap(getImage(img));
        } catch (Exception e){

        }

    }

    public void changePicture(View view) {
        Intent camera_intent
                = new Intent(MediaStore
                .ACTION_IMAGE_CAPTURE);

        startActivityForResult(camera_intent, pic_id);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");
            // Set the image in imageview for display
            profilepic.setImageBitmap(photo);
            byte[] image = getBytes(photo);
            db.updateDp(user.get(DBHelper.USER_ID),image);
            Log.d("DP ",user.get(DBHelper.USER_ID));

        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void updateProfile(View view) {
        String name = mDisplayname.getText().toString();
        db.updateDisplayname(user.get("id"),name);
        finish();
    }
}