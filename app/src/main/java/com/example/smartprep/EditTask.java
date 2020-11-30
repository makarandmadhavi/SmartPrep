package com.example.smartprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditTask extends AppCompatActivity {
    EditText taskview;
    String task;
    String project_id;
    String id;
    String status;
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

    }

    public void updatetask(View view) {
        task = taskview.getText().toString();
        DBHelper db = new DBHelper(this);
        db.inserttask(task, Integer.parseInt(project_id),Integer.parseInt(id),Integer.parseInt(status));
        finish();

    }
}