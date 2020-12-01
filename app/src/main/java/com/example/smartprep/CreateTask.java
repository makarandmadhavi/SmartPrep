package com.example.smartprep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class CreateTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
    int projectid;
    TextView mDateTime;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String timestamp;
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
    }

    public void createTask(View view) {
        EditText task = findViewById(R.id.tasktext);
        DBHelper db = new DBHelper(this);
        db.inserttask(task.getText().toString(),projectid,0,0,timestamp);
        finish();

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
}