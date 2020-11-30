package com.example.smartprep;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        EditText name = (EditText) findViewById(R.id.name);
        EditText usernamename = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        DBHelper db = new DBHelper(view.getContext());
        db.insertUser(name.getText().toString(),usernamename.getText().toString(),password.getText().toString(),0);
        finish();
    }
}