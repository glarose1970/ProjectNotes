package com.suncoastsoftware.projectnotes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.suncoastsoftware.projectnotes.Project;

import java.util.List;

/**
 * Created by Command Center on 10/26/2017.
 */

public class ViewNotesAdapterHelper extends RecyclerView.Adapter<ViewNotesAdapterHelper.ViewNotesViewHolder> {

    private Context context;
    private List<Project> projectList;

    public ViewNotesAdapterHelper(Context _context, List<Project> _projectList) {
        this.context = _context;
        this.projectList = _projectList;
    }

    @Override
    public ViewNotesAdapterHelper.ViewNotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewNotesAdapterHelper.ViewNotesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewNotesViewHolder extends RecyclerView.ViewHolder {

        public ViewNotesViewHolder(View itemView) {
            super(itemView);
        }
    }
}
