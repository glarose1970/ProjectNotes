package com.suncoastsoftware.projectnotes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suncoastsoftware.projectnotes.Project;
import com.suncoastsoftware.projectnotes.R;

import java.util.List;

/**
 * Created by Command Center on 10/28/2017.
 */

public class ViewProjectsAdapterHelper extends RecyclerView.Adapter<ViewProjectsAdapterHelper.ViewProjectsViewHolder> {

    private Context context;
    private List<Project> projectList;
    String thisCount = "";
    //private FirebaseDatabaseHelper dataHelper = new FirebaseDatabaseHelper();

    public ViewProjectsAdapterHelper(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public ViewProjectsAdapterHelper.ViewProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_projects_row_item, parent, false);
        return new ViewProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewProjectsAdapterHelper.ViewProjectsViewHolder holder, int position) {

        Project project = projectList.get(position);
        holder.tv_projectName.setText(project.getProjectName());
        holder.tv_projectAuthor.setText(project.getAuthor());
        holder.tv_projectStatus.setText(project.getProjectStatus());
        FirebaseDatabase mData = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mData.getReference("projects").child(String.valueOf(project.getProjectId())).child("notes");
        mRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               long mCount = dataSnapshot.getChildrenCount();
               thisCount = String.valueOf(mCount);
               holder.tv_noteCount.setText("Note Count - [" + thisCount + "]");
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    class ViewProjectsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_projectName, tv_projectAuthor, tv_projectStatus, tv_noteCount;
        Button btn_view;

        public ViewProjectsViewHolder(View itemView) {
            super(itemView);

            tv_projectName   = (TextView) itemView.findViewById(R.id.view_projects_tv_projectName);
            tv_projectStatus = (TextView) itemView.findViewById(R.id.view_projects_tv_projectStatus);
            tv_projectAuthor = (TextView) itemView.findViewById(R.id.view_projects_tv_projectAuthor);
            tv_noteCount     = (TextView) itemView.findViewById(R.id.view_projects_tv_projectNoteCount);
            btn_view         = (Button) itemView.findViewById(R.id.view_projects_btn_view);

        }
    }
}
