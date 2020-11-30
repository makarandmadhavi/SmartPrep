package com.example.smartprep;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProject extends AppCompatActivity {
    private EditText projectname;
    private EditText projectdesc;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("id").toString());
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        projectname = findViewById(R.id.tasktext);
        projectdesc = findViewById(R.id.edit_project_description);
        projectname.setText(name);
        projectdesc.setText(description);

    }

    public void updateProject(View view) {
        DBHelper db = new DBHelper(this);
        db.insertProject(projectname.getText().toString(),projectdesc.getText().toString(),id);
        finish();
    }
}