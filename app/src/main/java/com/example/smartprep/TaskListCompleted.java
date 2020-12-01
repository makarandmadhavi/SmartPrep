package com.example.smartprep;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchUIUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskListCompleted extends RecyclerView.Adapter<TaskListCompleted.TaskViewHolder> {
    private ArrayList<HashMap<String,String>> tasks;
    private LayoutInflater mInflater;
    private String api = "AIzaSyC0oq9-CkffD9yx56bkNcivkEZoesQT1KI";
    TaskListCompleted mAdapter2;
    public  String project_id;
    Context context;

    public TaskListCompleted(Context context, ArrayList<HashMap<String, String>> tasks,String project_id) {
        mInflater = LayoutInflater.from(context);
        this.tasks = tasks;
        this.context=context;
        this.project_id = project_id;
    }


    @NonNull
    @Override
    public TaskListCompleted.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mItemView = mInflater.inflate(R.layout.task_list_item_completed,
                viewGroup, false);
        return new TaskViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListCompleted.TaskViewHolder taskViewHolder, int i) {
        HashMap<String,String> mCurrent = tasks.get(i);
        taskViewHolder.task.setText(mCurrent.get("task"));
        taskViewHolder.id = mCurrent.get("id");
        taskViewHolder.status = mCurrent.get("status");
        taskViewHolder.taskdata = new HashMap<String, String>(mCurrent);
        if(taskViewHolder.status.equals("1")){
            taskViewHolder.check.setChecked(true);
        }


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView task;
        final TaskListCompleted mAdapter;
        public final TextView taskOption;
        public String id;
        public String status;
        public CheckBox check;
        public HashMap<String,String> taskdata;

        public TaskViewHolder(@NonNull View itemView, TaskListCompleted adapter) {
            super(itemView);
            DBHelper db = new DBHelper(context);
            task = itemView.findViewById(R.id.tasktext);
            task.setOnClickListener(this);
            this.mAdapter = adapter;
            taskOption = itemView.findViewById(R.id.taskoptions);
            taskOption.setOnClickListener(this);

            check = itemView.findViewById(R.id.isdone);
            check.setOnClickListener(this::onCheck);

        }

        private void onCheck(View view) {
            boolean isChecked = check.isChecked();
            DBHelper db = new DBHelper(context);
            if ( isChecked )
            {
                db.inserttask(task.getText().toString(),Integer.parseInt(project_id) ,Integer.parseInt(id),1,taskdata.get(DBHelper.TASK_TIMESTAMP));
            }
            else{
                db.inserttask(task.getText().toString(),Integer.parseInt(project_id) ,Integer.parseInt(id),0,taskdata.get(DBHelper.TASK_TIMESTAMP));

            }
            ((Project)context).onResume();
        }


        @Override
        public void onClick(View v) {
            if(v==task){
                //
                return;
            }
            TextView buttonViewOption = itemView.findViewById(R.id.taskoptions);
            PopupMenu popup = new PopupMenu(context, buttonViewOption);
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent;
                    switch (item.getItemId()) {
                        case R.id.open:
                            //handle menu1 click

                            return true;
                        case R.id.edit:
                            //handle menu2 click
                            intent = new Intent(context,EditTask.class);
                            intent.putExtra("task",task.getText().toString());
                            intent.putExtra("id",id);
                            intent.putExtra("projectid",project_id);
                            intent.putExtra("status",status);
                            context.startActivity(intent);
                            return true;
                        case R.id.remove:
                            int mPosition = getLayoutPosition();
                            // Use that to access the affected item in mWordList.
                            DBHelper db = new DBHelper(context);
                            db.deletetask(id);
                            tasks.remove(mPosition);
                            // Change the word in the mWordList.
                            //mProjects.set(mPosition, "Clicked! " + element);
                            mAdapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }
            });
            //displaying the popup
            popup.show();
        }
    }
}
