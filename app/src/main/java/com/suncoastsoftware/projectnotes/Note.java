package com.suncoastsoftware.projectnotes;

/**
 * Created by Command Center on 10/22/2017.
 */

public class Note {

    public String noteId;
    public String projectId;
    public String task;
    public String created;
    public String noteStatus;
    public String author;


    public Note() {
    }

    public Note(String noteId, String projectId, String task, String created, String noteStatus, String author) {
        this.noteId = noteId;
        this.projectId = projectId;
        this.task = task;
        this.created = created;
        this.noteStatus = noteStatus;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
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
