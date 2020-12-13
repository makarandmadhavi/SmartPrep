package com.example.smartprep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final int PICK_PHOTO_FOR_AVATAR = 12;
    int projectid;
    TextView mDateTime;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String timestamp;
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    EditText showLocation;
    LocationManager locationManager;
    double latitude;
    double longitude;
    ImageView taskpic;
    byte[] img;
    Bitmap bmp =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        projectid = Integer.parseInt(intent.getStringExtra("projectid"));
        TextView n = findViewById(R.id.projectname);
        n.setText(name);
        mDateTime = findViewById(R.id.datetime);
        timestamp = "Not set";
        showLocation = findViewById(R.id.showLocation);
        taskpic = findViewById(R.id.taskpic);

    }

    public void createTask(View view) {
        EditText task = findViewById(R.id.tasktext);
        DBHelper db = new DBHelper(this);
        db.inserttask(task.getText().toString(), projectid, 0, 0, timestamp, showLocation.getText().toString(),img);
        showNotification(this, "SmartPrep", "Reminder for: " + task.getText().toString(), new Intent());
        finish();

    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        timestamp = myday + "/" + myMonth + "/" + myYear + "\n" +  String.format("%02d", myHour) + ":" + String.format("%02d", myMinute);
        mDateTime.setText("Reminder: "+timestamp);
    }

    public void setDateTime(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    public void addLocation(View view) {
        //Creating the instance of PopupMenu
        Button buttonViewOption = findViewById(R.id.locationbutton);
        PopupMenu popup = new PopupMenu(this, buttonViewOption);
        //inflating menu from xml resource
        popup.inflate(R.menu.location_options);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                GPSTracker gpstracker;
                switch (item.getItemId()) {
                    case R.id.current:
                        //handle menu1 click
                        gpstracker = new GPSTracker(CreateTask.this);
                        latitude = 19.2163789;
                        longitude = 72.9618969;
                        showLocation.setText(latitude + "," + longitude);
                        //showLocation.setText(latitude+":"+longitude);
                        return true;
                    case R.id.search:
                        //handle menu2
                        gpstracker = new GPSTracker(CreateTask.this);
                        latitude = gpstracker.getLatitude();
                        longitude = gpstracker.getLongitude();
                        Uri gmmIntentUri = Uri.parse("geo:19.2163789,72.9618969" + "?q=" + Uri.encode(showLocation.getText().toString()));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        //displaying the popup
        popup.show();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            InputStream inputStream=null;
            try {
                inputStream = this.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
            bmp = BitmapFactory.decodeStream(inputStream);
            taskpic.setImageBitmap(bmp);
            img = getBytes(bmp);
        }
    }

    public void selectImage(View view) {
        pickImage();
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

    public void openImage(View view) {
//        Intent intent = new Intent(this,Viewimage.class);
//        intent.putExtra("id",id);
//        startActivity(intent);
        return;
    }
}