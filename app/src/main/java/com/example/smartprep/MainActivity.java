package com.example.smartprep;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ArrayList<HashMap<String,String>> projects = new ArrayList<HashMap<String,String>>();
    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;
    public DBHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HashMap user = mydb.checklogin();
        if(user==null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Logged in",
                    Toast.LENGTH_LONG).show();
        }


        projects = mydb.getAllProjects();

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ProjectListAdapter(this, projects);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                int wordListSize = projects.size();
////                // Add a new word to the wordList.
////                HashMap<String,String> item = new HashMap<>();
////                item.put("name","Test pro" + wordListSize);
////                item.put("description","Test description " + wordListSize);
////                projects.add(item);
////                mydb.insertProject("Test pro" + wordListSize,"Test description" + wordListSize);
////                // Notify the adapter, that the data has changed.
////                mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
////                // Scroll to the bottom.
////                mRecyclerView.smoothScrollToPosition(wordListSize);
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mydb.logout();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createProject(View view) {
        Intent intent = new Intent(view.getContext(), CreateProject.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        projects = mydb.getAllProjects();
        mAdapter = new ProjectListAdapter(this, projects);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}