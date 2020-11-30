package com.example.smartprep;

import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder> {

    private final ArrayList<HashMap<String, String>> mProjects;
    private LayoutInflater mInflater;
    private Context context;

    public ProjectListAdapter(Context context, ArrayList<HashMap<String, String>> mProjects) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mProjects = mProjects;
    }

    @NonNull
    @Override
    public ProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.project_list_item, viewGroup, false);
        return new ProjectViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListAdapter.ProjectViewHolder projectViewHolder, int i) {
        HashMap<String, String> mCurrent = mProjects.get(i);
        projectViewHolder.projectName.setText(mCurrent.get("name"));
        projectViewHolder.projectDescription.setText(mCurrent.get("description"));
        projectViewHolder.id = mCurrent.get("id");
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView projectName;
        public final TextView projectDescription;
        final ProjectListAdapter mAdapter;
        public final TextView buttonViewOption;
        public String id;

        public ProjectViewHolder(View itemView, ProjectListAdapter adapter) {
            super(itemView);
            projectName = itemView.findViewById(R.id.project);
            projectDescription = itemView.findViewById(R.id.description);
            buttonViewOption = itemView.findViewById(R.id.projectoptions);
            this.mAdapter = adapter;
            projectName.setOnClickListener(this);
            buttonViewOption.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //creating a popup menu
            if(v == projectName){
                Intent intent = new Intent(context,Project.class);
                intent.putExtra("id",id);
                intent.putExtra("name",projectName.getText().toString());
                intent.putExtra("description",projectDescription.getText().toString());
                context.startActivity(intent);
                return;
            }
            TextView buttonViewOption = itemView.findViewById(R.id.projectoptions);
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
                            intent = new Intent(context,Project.class);
                            intent.putExtra("id",id);
                            intent.putExtra("name",projectName.getText().toString());
                            intent.putExtra("description",projectDescription.getText().toString());
                            context.startActivity(intent);
                            return true;
                        case R.id.edit:
                            //handle menu2 click
                            intent = new Intent(projectName.getContext(), EditProject.class);
                            intent.putExtra("id",id);
                            intent.putExtra("name",projectName.getText().toString());
                            intent.putExtra("description",projectDescription.getText().toString());
                            context.startActivity(intent);
                            return true;
                        case R.id.remove:
                            int mPosition = getLayoutPosition();
                            // Use that to access the affected item in mWordList.
                            DBHelper db = new DBHelper(context);
                            db.deleteProject(id);
                            mProjects.remove(mPosition);
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
            // Get the position of the item that was clicked.
//            int mPosition = getLayoutPosition();
//// Use that to access the affected item in mWordList.
//            HashMap<String, String> element = mProjects.get(mPosition);
//            DBHelper db = new DBHelper(context);
//
//            TextView proj = v.findViewById(R.id.project);
//            String projr = (String) proj.getText();
//            db.deleteProject(projr);
//            mProjects.remove(mPosition);
//// Change the word in the mWordList.
//            //mProjects.set(mPosition, "Clicked! " + element);
//            mAdapter.notifyDataSetChanged();
        }
    }
}
