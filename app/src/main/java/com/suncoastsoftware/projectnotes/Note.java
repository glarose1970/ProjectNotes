package com.suncoastsoftware.projectnotes;

/**
 * Created by Command Center on 10/22/2017.
 */

public class Note {

    public int noteId;
    public int projectId;
    public String task;
    public String created;
    public String noteStatus;

    public Note() {
    }

    public Note(int noteId, int projectId, String task, String created, String status) {
        this.noteId = noteId;
        this.projectId = projectId;
        this.task = task;
        this.created = created;
        this.noteStatus = status;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }
}
