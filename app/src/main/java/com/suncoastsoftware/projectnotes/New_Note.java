package com.suncoastsoftware.projectnotes;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class New_Note extends Fragment {
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

    Button btn_add_note, btn_cancel;
    Spinner spinnerProject, spinnerStatus;
    EditText et_noteMessage, et_author, et_created, et_noteId;

    private List<Project> projectList;
    private Note note;
    private String projectName;

    public New_Note() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static New_Note newInstance(String param1, String param2) {
        New_Note fragment = new New_Note();
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
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        spinnerProject = (Spinner) view.findViewById(R.id.new_note_spinner_projects);
        spinnerStatus  = (Spinner) view.findViewById(R.id.new_note_spinner_status);
        et_noteMessage = (EditText) view.findViewById(R.id.new_note_et_newNoteTask);
        et_author      = (EditText) view.findViewById(R.id.new_note_et_author);
        et_created     = (EditText) view.findViewById(R.id.new_note_et_created);
        et_noteId      = (EditText) view.findViewById(R.id.new_note_et_noteId);
        btn_add_note   = (Button) view.findViewById(R.id.new_note_btn_addNote);
        btn_cancel     = (Button) view.findViewById(R.id.new_note_btn_cancel);
        projectList    = new ArrayList<>();

        et_noteId.setText("11-" + GenerateID());
        String[] statusValues = new String[] {"Choose Status", "Active", "Complete", "On Hold"};
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, statusValues);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Choose Status")) {

                }else {

                    projectName = spinnerStatus.getSelectedItem().toString();
                    // new SaveNote().execute(spinnerProject.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().equals("Choose Project")) {

                }else {

                    projectName = spinnerProject.getSelectedItem().toString();
                   // new SaveNote().execute(spinnerProject.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerStatus.getSelectedItem().toString().equals("Choose Status")) {
                    Toast.makeText(getContext(), "Choose a Status", Toast.LENGTH_SHORT).show();
                }else {
                    new SaveNote().execute(projectName);
                }
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

    private int GenerateID() {

        int id = 0;
        int preId = 11;
        String dash = "-";

        Random rand = new Random();
        int min = 000000;
        int max = 999999;

        id = rand.nextInt((max + 1 - min) + min);

        return id;

    }

    class SaveNote extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... params) {

            mRef.child("projects").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                    for (DataSnapshot child : data) {
                        String name = child.child("projectName").getValue().toString();
                        if (name.equals(params[0])) {
                            String id = child.child("projectId").getValue().toString();
                            String noteID = et_noteId.getText().toString();
                            note = new Note(noteID, id, et_noteMessage.getText().toString(), et_created.getText().toString(),
                                    spinnerStatus.getSelectedItem().toString(), et_author.getText().toString());
                            mRef.child("projects").child(id).child("notes").child(noteID).setValue(note);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }


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
            spinnerProject.setAdapter(adapter);
           // spinnerProject.setSelection(0);
        }
    }
}
