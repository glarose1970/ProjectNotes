package com.suncoastsoftware.projectnotes;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Command Center on 10/25/2017.
 */

public class FirebaseDatabaseHelper {

    static FirebaseDatabase mData = FirebaseDatabase.getInstance();
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static DatabaseReference mRef = mData.getReference();
    FirebaseUser mUser = mAuth.getCurrentUser();

    int count = 0;
    static String firstChild;
    String nCount;
    //Project project;

    public FirebaseDatabaseHelper() {

    }
    public List<Project> GetProjects() {

        final List<Project> projectNameList = new ArrayList<>();
        mRef.child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
               for (DataSnapshot data : children) {
                   Project project = data.getValue(Project.class);
                   projectNameList.add(project);
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return projectNameList;
    }

    public static String getNoteCount(String projectId) {

        try {
            mRef.child("projects").child(projectId).child("notes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long notes = dataSnapshot.getChildrenCount();
                    firstChild = String.valueOf(notes);
                    Log.d("FirebaseHelper :", "Note Count : " + firstChild);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e) {
            Log.d("FirebaseHelper :", "Error : " + e.getMessage());
        }

        return firstChild;
    }

}
