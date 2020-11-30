package com.example.smartprep;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "dbhelper";

    public static final String DATABASE_NAME = "ProjectsDB.db";
    public static final String PROJECTS_TABLE_NAME = "projects";
    public static final String PROJECTS_ID = "id";
    public static final String PROJECTS_NAME = "name";
    public static final String PROJECTS_DESCRIPTION = "description";

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_USERNAME = "username";
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String USER_PASSWORD = "password";

    public static final String TASK_TABLE_NAME = "tasks";
    public static final String TASK_TEXT = "task";
    public static final String TASK_ID = "id";
    public static final String TASK_PROJECT = "project";
    public static final String TASK_STATUS = "status";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table projects " +
                        "(id integer primary key, name text,description text,userid text);"
        );
        db.execSQL(
                "create table user " +
                        "(id integer primary key,name text,username text,password text);"
        );
        db.execSQL(
                "create table login " +
                        "(id integer primary key,name text,username text,password text);"
        );
        db.execSQL(
                "create table tasks " +
                        "(id integer primary key,task text,project integer,status integer);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS projects");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    public void login (String name, String username,String password , int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_USERNAME, username);
        contentValues.put(USER_PASSWORD, password);
        contentValues.put(USER_ID,id);
        db.insertWithOnConflict("login", null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG,"inserted" + name);
    }

    public void logout () {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"logout");
        db.execSQL("DELETE from login");

    }

    public HashMap<String,String> checklogin() {
        //ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from login", null );
        res.moveToFirst();
        if(res.getCount() > 0){
            HashMap<String,String> hm= new HashMap<String,String>();
            hm.put(USER_USERNAME, res.getString(res.getColumnIndex(USER_USERNAME)));
            hm.put(USER_NAME, res.getString(res.getColumnIndex(USER_NAME)));
            hm.put(USER_ID, res.getString(res.getColumnIndex(USER_ID)));
            return hm;
        }
        return null;
    }

    public boolean insertProject (String name, String description, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROJECTS_NAME, name);
        contentValues.put(PROJECTS_DESCRIPTION, description);
        if(id!=0){
            contentValues.put(PROJECTS_ID,id);
        }
        db.insertWithOnConflict(PROJECTS_TABLE_NAME, null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG,"inserted" + name);
        return true;
    }


    public Integer deleteProject (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"deleted" + id);
        db.delete(TASK_TABLE_NAME,
                "project = ? ",
                new String[] { id });
        return db.delete(PROJECTS_TABLE_NAME,
                "id = ? ",
                new String[] { id });

    }
//
    public ArrayList<HashMap<String, String>> getAllProjects() {

        ArrayList<HashMap<String,String>> array_list = new ArrayList<HashMap<String,String>>();
        //ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from projects", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            HashMap<String,String> hm= new HashMap<String,String>();
            hm.put(PROJECTS_NAME, res.getString(res.getColumnIndex(PROJECTS_NAME)));
            hm.put(PROJECTS_DESCRIPTION, res.getString(res.getColumnIndex(PROJECTS_DESCRIPTION)));
            hm.put(PROJECTS_ID, res.getString(res.getColumnIndex(PROJECTS_ID)));
            array_list.add(hm);
            ///array_list.add(res.getString(res.getColumnIndex(PROJECTS_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean insertUser (String name, String username,String password , int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_USERNAME, username);
        contentValues.put(USER_PASSWORD, password);
        if(id!=0){
            contentValues.put(USER_ID,id);
        }
        db.insertWithOnConflict(USER_TABLE_NAME, null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG,"inserted" + name);
        return true;
    }

    public HashMap<String, String> getUser(String username, String password) {
        //ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where "+USER_USERNAME+"='"+username+"' and password='"+password+"'", null );
        res.moveToFirst();
        if(res.getCount() > 0){
            HashMap<String,String> hm= new HashMap<String,String>();
            hm.put(USER_USERNAME, res.getString(res.getColumnIndex(USER_USERNAME)));
            hm.put(USER_NAME, res.getString(res.getColumnIndex(USER_NAME)));
            hm.put(USER_ID, res.getString(res.getColumnIndex(USER_ID)));
            return hm;
        }
        return null;
    }

    public boolean inserttask (String task, int project, int id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_TEXT, task);
        contentValues.put(TASK_PROJECT, project);
        contentValues.put(TASK_STATUS, status);
        if(id!=0){
            contentValues.put(TASK_ID,id);
        }
        db.insertWithOnConflict(TASK_TABLE_NAME, null,contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG,"inserted" + task);
        return true;
    }

    public Integer deletetask (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG,"deleted" + id);
        return db.delete(TASK_TABLE_NAME,
                "id = ? ",
                new String[] { id });

    }

    public ArrayList<HashMap<String, String>> gettasks(int projectid,int status) {

        ArrayList<HashMap<String,String>> array_list = new ArrayList<HashMap<String,String>>();
        //ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from tasks where "+TASK_PROJECT+"="+projectid+" AND status="+status, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            HashMap<String,String> hm= new HashMap<String,String>();
            hm.put(TASK_ID, res.getString(res.getColumnIndex(TASK_ID)));
            hm.put(TASK_TEXT, res.getString(res.getColumnIndex(TASK_TEXT)));
            hm.put(TASK_STATUS, res.getString(res.getColumnIndex(TASK_STATUS)));
            array_list.add(hm);
            ///array_list.add(res.getString(res.getColumnIndex(PROJECTS_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
