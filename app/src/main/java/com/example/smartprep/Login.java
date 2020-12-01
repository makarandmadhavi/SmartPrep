package com.example.smartprep;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    DBHelper db = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.greeting);
        EditText password = (EditText) findViewById(R.id.password);
        HashMap<String,String> user = db.getUser(username.getText().toString(),password.getText().toString());

        if(user!=null){
            db.login(user.get("name"),user.get("username"),user.get("password"),Integer.parseInt(user.get("id")),db.getDp(user.get("id")));
            finish();
        }
        else {
            Toast.makeText(this, "Incorrect credentials",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void register(View view) {
        Intent intent = new Intent(view.getContext(),Register.class);
        startActivity(intent);
    }
}