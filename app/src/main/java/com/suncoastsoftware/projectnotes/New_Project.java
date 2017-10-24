package com.suncoastsoftware.projectnotes;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class New_Project extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button btn_createProject, btn_cancel;
    private EditText txt_projectName, txt_desc, txt_createdDate, txt_author, txt_status;

    //set up Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mUser;

    public New_Project() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static New_Project newInstance(String param1, String param2) {
        New_Project fragment = new New_Project();
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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("projects");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_new_project, container, false);
        txt_projectName = (EditText) view.findViewById(R.id.et_projectName);
        txt_desc        = (EditText) view.findViewById(R.id.et_projectDesc);
        txt_author      = (EditText) view.findViewById(R.id.et_projectAuthor);
        txt_createdDate = (EditText) view.findViewById(R.id.et_projectCreated);
        txt_status      = (EditText) view.findViewById(R.id.et_projectStatus);

        btn_createProject = (Button) view.findViewById(R.id.new_project_btn_addProject);
        btn_cancel        = (Button) view.findViewById(R.id.new_project_btn_cancel);

        btn_createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProject(txt_projectName.getText().toString(), txt_desc.getText().toString(), txt_createdDate.getText().toString(), txt_author.getText().toString(), txt_status.getText().toString());
            }
        });
        return view;
    }

    private void createProject(String projectName, String projectDesc, String projectCreated, String projectAuthor, String projectStatus) {
        if (Validate_Input()) {
            //Toast.makeText(getContext(), "all filled in", Toast.LENGTH_SHORT).show();
            int projectId = GenerateID();
            Project project = new Project(projectId, projectName, projectDesc, projectCreated, projectAuthor, projectStatus);
            mRef.child(mUser.getUid()).push().setValue(project);
            Clear_Input();
        }else {
            //Toast.makeText(getContext(), "fields required", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Validate_Input() {

        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.new_project_layout_input);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof LinearLayout) {
                for (int y = 0; y < ((LinearLayout) v).getChildCount(); y++) {
                    View c = ((LinearLayout) v).getChildAt(y);
                    if (c instanceof EditText) {
                        if (TextUtils.isEmpty(((EditText) c).getText().toString())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void Clear_Input() {
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.new_project_layout_input);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof LinearLayout) {
                for (int y = 0; y < ((LinearLayout) v).getChildCount(); y++) {
                    View c = ((LinearLayout) v).getChildAt(y);
                    if (c instanceof EditText) {
                       ((EditText) c).setText("");
                    }
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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

    //generate a unique id for the project
    private int GenerateID() {

        int id = 0;
        int preId = 11;
        String dash = "-";

        Random rand = new Random();
        int min = 000000;
        int max = 999999;

        id = rand.nextInt((max - min) + max) + 1;

        return id;

    }
}
