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

public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    EditText taskview;
    String task;
    String project_id;
    String id;
    String status;
    TextView mDateTime;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent = getIntent();
        taskview = (EditText) findViewById(R.id.tasktext);
        taskview.setText(intent.getStringExtra("task"));
        project_id = intent.getStringExtra("projectid");
        id = intent.getStringExtra("id");
        status = intent.getStringExtra("status");
        mDateTime = findViewById(R.id.datetime);
        mDateTime.setText("Reminder: "+intent.getStringExtra(DBHelper.TASK_TIMESTAMP));

    }

    public void updatetask(View view) {
        task = taskview.getText().toString();
        DBHelper db = new DBHelper(this);
        db.inserttask(task, Integer.parseInt(project_id),Integer.parseInt(id),Integer.parseInt(status),timestamp);
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