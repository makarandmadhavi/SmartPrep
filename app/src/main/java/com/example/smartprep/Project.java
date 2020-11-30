package com.example.smartprep;

//import android.app.Activity;
//import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.gms.common.api.Status;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.net.PlacesClient;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.AutocompleteActivity;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Project extends AppCompatActivity {
    private ArrayList<HashMap<String,String>> tasks = new ArrayList<HashMap<String,String>>();
    private ArrayList<HashMap<String,String>> completed = new ArrayList<HashMap<String,String>>();
    private RecyclerView mRecyclerView;
    private RecyclerView mCompletedRecycler;
    private TaskListAdapter mAdapter;
    private TaskListCompleted mAdapter2;
    private TextView projectname;
    private String project_id;
    int PLACE_PICKER_REQUEST = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private String api = "AIzaSyC0oq9-CkffD9yx56bkNcivkEZoesQT1KI";
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        db = new DBHelper(this);

        Intent intent = getIntent();
        projectname = findViewById(R.id.projname);
        projectname.setText(intent.getStringExtra("name"));
        project_id = intent.getStringExtra("id");
        // Get a handle to the RecyclerView.
        createRecyclerView();
    }

    public void createRecyclerView() {
        tasks = db.gettasks(Integer.parseInt(project_id),0);
        //Pending
        mRecyclerView = findViewById(R.id.taskview);
        mAdapter = new TaskListAdapter(this, tasks,project_id);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        //completed
        completed = db.gettasks(Integer.parseInt(project_id),1);
        mCompletedRecycler = findViewById(R.id.completed);
        mAdapter2 = new TaskListCompleted(this, completed,project_id);
        mCompletedRecycler.setAdapter(mAdapter2);
        mCompletedRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCompletedRecycler.setNestedScrollingEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        createRecyclerView();

    }

    public void createtask(View view) {
        Intent intent = new Intent(this,CreateTask.class);
        intent.putExtra("projectid",project_id);
        intent.putExtra("name",projectname.getText().toString());
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i("Places", "Place: " + place.getName() + ", " + place.getId());
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i("Places", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}