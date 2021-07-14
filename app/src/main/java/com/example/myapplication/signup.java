package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    private EditText memail,mpass;
    private TextView mtextView;
    private Button signupbtn;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        memail = findViewById(R.id.emailsignin);
        mpass = findViewById(R.id.passsignup);
        mtextView = findViewById(R.id.mtextView);
        signupbtn = findViewById(R.id.signupbtn);

        mauth = FirebaseAuth.getInstance();

        mtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this,signin.class));
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    private void createUser(){
        String email =memail.getText().toString();
        String pass = mpass.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!pass.isEmpty()){
                mauth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(signup.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup.this, signin.class));
                                finish();
                            }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signup.this,"Registration Error",Toast.LENGTH_SHORT).show();
                    }
                });


            }else{
                mpass.setError("Empty Fields Are Not Allowed!!");
            }
        }else if(email.isEmpty()){
            memail.setError("Empty Fields Are Not Allowed!!");
        }else{
            memail.setError("Please Enter Correct Email");
        }


    }
}