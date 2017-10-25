package com.suncoastsoftware.projectnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText et_email;
    private EditText et_password;

    private Button btn_login;
    private Button btn_cancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
      //  mAuth.signOut();
        btn_login   = (Button) findViewById(R.id.btn_login);
        btn_cancel  = (Button) findViewById(R.id.btn_cancel);
        et_email    = (EditText) findViewById(R.id.et_email) ;
        et_password = (EditText) findViewById(R.id.et_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //check to see if the user is logged in
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //user is signed in
                    //navigate to the main content activity
                    Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void Login(String email, String password) {

        if (TextUtils.isEmpty(et_email.getText()) || TextUtils.isEmpty(et_password.getText())) {
            Toast.makeText(this, "All Fields Required!", Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(et_email.getText())) {
                et_email.requestFocus();
            }else {
                et_password.requestFocus();
            }
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
