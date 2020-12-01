package com.example.smartprep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import android.Manifest;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
    int projectid;
    TextView mDateTime;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String timestamp;
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    double latitude;
    double longitude;
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

    }

    public void createTask(View view) {
        EditText task = findViewById(R.id.tasktext);
        DBHelper db = new DBHelper(this);
        db.inserttask(task.getText().toString(),projectid,0,0,timestamp);
        showNotification(this,"SmartPrep","Reminder for: "+task.getText().toString(),new Intent());
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
        timestamp = "Reminder:\n"+ myday+"/"+myMonth+"/"+myYear+"\n"+myHour+":"+myMinute;
        mDateTime.setText(timestamp);
    }

    public void setDateTime(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,year, month,day);
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
                    switch (item.getItemId()) {
                        case R.id.current:
                            //handle menu1 click
                            GPSTracker  gpstracker=new GPSTracker(CreateTask.this);
                            latitude=gpstracker.getLatitude();
                            longitude=gpstracker.getLongitude();
                            showLocation.setText(latitude+":"+longitude);
                            Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                            return true;
                        case R.id.search:
                            //handle menu2
                            return true;
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();
    }

}