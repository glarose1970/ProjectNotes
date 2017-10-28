package com.suncoastsoftware.projectnotes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.suncoastsoftware.projectnotes.Note;
import com.suncoastsoftware.projectnotes.R;

import java.util.List;

/**
 * Created by Command Center on 10/26/2017.
 */

public class ViewNotesAdapterHelper extends RecyclerView.Adapter<ViewNotesAdapterHelper.ViewNotesViewHolder> {

    private Context context;
    private List<Note> noteList;

    public ViewNotesAdapterHelper(Context _context, List<Note> _noteList) {
        this.context = _context;
        this.noteList = _noteList;
    }

    @Override
    public ViewNotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_note_row_item, parent, false);

        return new ViewNotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewNotesViewHolder holder, int position) {

        Note note = noteList.get(position);
        holder.tv_projectName.setText(note.getProjectName());
        holder.tv_noteMessage.setText(note.getTask());
        holder.tv_status.setText(note.getNoteStatus());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewNotesViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_projectName, tv_noteMessage, tv_status;
        public Button btn_edit, btn_remove;

        public ViewNotesViewHolder(View itemView) {
            super(itemView);

            tv_projectName = (TextView) itemView.findViewById(R.id.new_note_row_projectName);
            tv_noteMessage = (TextView) itemView.findViewById(R.id.new_note_row_projectMessage);
            tv_status      = (TextView) itemView.findViewById(R.id.new_note_row_projectStatus);
            btn_edit       = (Button) itemView.findViewById(R.id.view_notes_btn_edit);
            btn_remove     = (Button) itemView.findViewById(R.id.view_notes_btn_remove);
        }
    }
}
