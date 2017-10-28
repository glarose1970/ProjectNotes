package com.suncoastsoftware.projectnotes;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suncoastsoftware.projectnotes.Adapters.ViewNotesAdapterHelper;

import java.util.ArrayList;
import java.util.List;

public class View_Notes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseDatabase mData = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mRef = mData.getReference();
    FirebaseUser mUser = mAuth.getCurrentUser();

    private OnFragmentInteractionListener mListener;
    private FirebaseDatabaseHelper DBHelper;
    private List<Project> projectList;
    List<Note> noteList;

    Spinner projectsSpinner;

    private RecyclerView notesRecView;
    private ViewNotesAdapterHelper viewNotesAdapter;


    public View_Notes() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static View_Notes newInstance(String param1, String param2) {
        View_Notes fragment = new View_Notes();
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



        new LoadProjects().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view__notes, container, false);
        projectList = new ArrayList<>();
        noteList = new ArrayList<>();
        projectsSpinner = (Spinner) view.findViewById(R.id.view_notes_spinner_projects);
        notesRecView    = (RecyclerView) view.findViewById(R.id.view_notes_recViewNotes);
        projectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().equals("Choose Project")) {

                }else {
                    //new LoadNotes().execute(parent.getSelectedItem().toString());
                    mRef.child("projects").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            noteList.clear();
                            Iterable<DataSnapshot>  data = dataSnapshot.getChildren();
                            for (DataSnapshot child : data) {
                                String name = child.child("projectName").getValue().toString();
                                if (name.equals(projectsSpinner.getSelectedItem())) {
                                    String id = child.child("projectId").getValue().toString();
                                    Iterable<DataSnapshot> notesData = child.child("notes").getChildren();
                                    for (DataSnapshot notes : notesData) {
                                        Note note = notes.getValue(Note.class);
                                        noteList.add(note);
                                    }
                                    //String test = "";
                                }
                            }
                            //set the adapter for the Recyclerview here.
                            viewNotesAdapter = new ViewNotesAdapterHelper(getContext(), noteList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            notesRecView.setLayoutManager(mLayoutManager);
                            notesRecView.setItemAnimator(new DefaultItemAnimator());
                            notesRecView.setAdapter(viewNotesAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    //I comment this method out because I don't know what it does and I get errors when running
  /*  @Override
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

    class LoadProjects extends AsyncTask<Void, Void, Void> {

        List<String> projectNameList = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            projectNameList.add("Choose Project");
            mRef.child("projects").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot data : children) {
                        Project project = data.getValue(Project.class);
                        projectList.add(project);
                    }

                    for (Project project : projectList) {
                        projectNameList.add(project.getProjectName());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, projectNameList);
            projectsSpinner.setAdapter(adapter);
            projectsSpinner.setSelection(0);
        }
    }
    // Load Notes when user chooses a project
    //params = projectID
    class LoadNotes extends AsyncTask<String, Void, Void> {

        List<Note> noteList = new ArrayList<>();
        @Override
        protected Void doInBackground(final String... params) {

            mRef.child("projects").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot>  data = dataSnapshot.getChildren();
                    for (DataSnapshot child : data) {
                        String name = child.child("projectName").getValue().toString();
                        if (name.equals(params[0])) {
                            String id = child.child("projectId").getValue().toString();
                            Iterable<DataSnapshot> notesData = child.child("notes").getChildren();
                            for (DataSnapshot notes : notesData) {
                                Note note = notes.getValue(Note.class);
                                noteList.add(note);
                            }
                            //String test = "";
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //set the adapter for the Recyclerview here.
            viewNotesAdapter = new ViewNotesAdapterHelper(getContext(), noteList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            notesRecView.setLayoutManager(mLayoutManager);
            notesRecView.setItemAnimator(new DefaultItemAnimator());
            notesRecView.setAdapter(viewNotesAdapter);
        }
    }
}
