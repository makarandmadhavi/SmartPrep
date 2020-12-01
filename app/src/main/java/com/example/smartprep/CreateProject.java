package com.example.smartprep;

//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateProject extends AppCompatActivity {
    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        name = (EditText) findViewById(R.id.tasktext);
        description = (EditText) findViewById(R.id.displayname);
    }

    public void createProject(View view) {
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();
        DBHelper db = new DBHelper(this);
        db.insertProject(name,description,0);
        finish();
    }
}