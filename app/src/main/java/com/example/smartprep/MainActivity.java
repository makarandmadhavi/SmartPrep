package com.example.smartprep;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String CHANNEL_ID = "1";
    private ArrayList<HashMap<String,String>> projects = new ArrayList<HashMap<String,String>>();
    private RecyclerView mRecyclerView;
    private ProjectListAdapter mAdapter;
    public DBHelper mydb;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recyclerview);


        DrawerLayout drawer = findViewById(R.id.drawer_layout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;

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

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        try{
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        } catch (Exception e){
            return null;
        }

    }
    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            super .onBackPressed() ;
        }
    }
    @SuppressWarnings ( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId() ;
        if (id == R.id. nav_home ) {
            // Handle the camera action
        } else if (id == R.id. nav_profile ) {
            Intent intent = new Intent(this,Profile.class);
            startActivity(intent);

        } else if (id == R.id. nav_logout ) {
            mydb.logout();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout ) ;
        drawer.closeDrawer(GravityCompat.START ) ;
        return true;
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
        HashMap<String,String> user = mydb.checklogin();
        NavigationView navigationView = findViewById(R.id.nav_view ) ;
        navigationView.setNavigationItemSelectedListener( this ) ;
        View headerView = navigationView.getHeaderView(0);
        TextView greeting = (TextView) headerView.findViewById(R.id.greeting);
        greeting.setText("Hello !");
        ImageView dp = headerView.findViewById(R.id.dp);
        if(user==null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Logged in",
                    Toast.LENGTH_LONG).show();
            greeting.setText("Hello "+user.get("name")+"!");

            dp.setImageBitmap(getImage(mydb.getDp(user.get("id"))));
            projects = mydb.getAllProjects();
            // Create an adapter and supply the data to be displayed.
            mAdapter = new ProjectListAdapter(this, projects);
            // Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}