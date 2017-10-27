package com.suncoastsoftware.projectnotes;

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

    FirebaseDatabase mData = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mRef = mData.getReference();
    FirebaseUser mUser = mAuth.getCurrentUser();

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

}
