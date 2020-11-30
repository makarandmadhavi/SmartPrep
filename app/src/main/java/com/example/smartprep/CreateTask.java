package com.example.smartprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTask extends AppCompatActivity {
    int projectid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        projectid = Integer.parseInt(intent.getStringExtra("projectid"));
        TextView n = findViewById(R.id.projectname);
        n.setText(name);
    }

    public void createTask(View view) {
        EditText task = findViewById(R.id.tasktext);
        DBHelper db = new DBHelper(this);
        db.inserttask(task.getText().toString(),projectid,0,0);
        finish();

    }
}