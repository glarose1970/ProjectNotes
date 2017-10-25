package com.suncoastsoftware.projectnotes;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class New_Project extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String status;

    private OnFragmentInteractionListener mListener;

    private Button btn_createProject, btn_cancel;
    private EditText txt_projectName, txt_desc, txt_createdDate, txt_author;
    private Spinner statusSpinner;

    //Date Picker Dialog
    private DatePickerDialog.OnDateSetListener mDatSetListener;
    private List<String> statusList;


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
        mRef = mDatabase.getReference("projects").child(mUser.getUid());
        statusList = new ArrayList<>();
        LoadStatus();

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
        statusSpinner   = (Spinner) view.findViewById(R.id.new_project_spinner_status);

        btn_createProject = (Button) view.findViewById(R.id.new_project_btn_addProject);
        btn_cancel        = (Button) view.findViewById(R.id.new_project_btn_cancel);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statusList);
        statusSpinner.setAdapter(statusAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String curItem = parent.getItemAtPosition(position).toString();
                if (curItem.equalsIgnoreCase("Choose Status")) {
                    status = "Active";
                }else {
                    status = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProject(txt_projectName.getText().toString(), txt_desc.getText().toString(), txt_createdDate.getText().toString(),
                              txt_author.getText().toString(), statusSpinner.getSelectedItem().toString());
            }
        });

        txt_createdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCal = Calendar.getInstance();
                int year = mCal.get(Calendar.YEAR);
                int month = mCal.get(Calendar.MONTH);
                int day = mCal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light,
                        mDatSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDatSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                txt_createdDate.setText(date);
          }
        };
        return view;
    }

    private void createProject(String projectName, String projectDesc, String projectCreated, String projectAuthor, String projectStatus) {
        if (Validate_Input()) {
            //Toast.makeText(getContext(), "all filled in", Toast.LENGTH_SHORT).show();
            int projectId = GenerateID();
            Project project = new Project(projectId, projectName, projectDesc, projectCreated, projectAuthor, projectStatus);
            mRef.child(String.valueOf(projectId)).setValue(project);
            Clear_Input();
            statusSpinner.setSelection(0);
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

        id = rand.nextInt((max - min) + max);

        return id;

    }
    private List<String> LoadStatus() {

        statusList.add("Choose Status");
        statusList.add("Active");
        statusList.add("Complete");
        statusList.add("On Hold");

        return statusList;
    }
}
