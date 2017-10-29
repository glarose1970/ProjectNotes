package com.suncoastsoftware.projectnotes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suncoastsoftware.projectnotes.Adapters.ViewProjectsAdapterHelper;

import java.util.ArrayList;
import java.util.List;

public class View_Projects extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //setup recyclerview
    private ViewProjectsAdapterHelper viewProjectsHelper;
    private RecyclerView projectsRecView;
    private List<Project> projectList;

    //setup firebase
    FirebaseDatabase mData;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public View_Projects() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static View_Projects newInstance(String param1, String param2) {
        View_Projects fragment = new View_Projects();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference("projects");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view__projects, container, false);

        projectList = new ArrayList<>();
        projectsRecView = (RecyclerView) view.findViewById(R.id.view_projects_projects_recView);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot child : data) {
                    Project project = child.getValue(Project.class);
                    projectList.add(project);
                }
                viewProjectsHelper = new ViewProjectsAdapterHelper(getContext(), projectList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                projectsRecView.setLayoutManager(mLayoutManager);
                projectsRecView.setItemAnimator(new DefaultItemAnimator());
                projectsRecView.setAdapter(viewProjectsHelper);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
