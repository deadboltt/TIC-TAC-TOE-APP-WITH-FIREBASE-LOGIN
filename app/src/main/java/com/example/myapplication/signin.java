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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity {

    private EditText memail,mpass;
    private TextView mtextView;
    private Button signinbtn;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        memail = findViewById(R.id.emailsignin);
        mpass = findViewById(R.id.passsignin);
        mtextView = findViewById(R.id.mtextView);
        signinbtn = findViewById(R.id.signinbtn);

        mauth = FirebaseAuth.getInstance();
        mtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signin.this, signup.class));
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mauth.getCurrentUser() != null) {
            startActivity(new Intent(signin.this, MainActivity.class));
            finish();
        }
    }

    private void loginUser() {
        String email = memail.getText().toString().trim();
        String pass = mpass.getText().toString();

        if (email.isEmpty()) {
            memail.setError("Empty Fields Are Not Allowed!!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            memail.setError("Please Enter Correct Email");
            return;
        }

        if (pass.isEmpty()) {
            mpass.setError("Empty Fields Are Not Allowed!!");
            return;
        }

        signinbtn.setEnabled(false);
        mauth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        signinbtn.setEnabled(true);
                        Toast.makeText(signin.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(signin.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signinbtn.setEnabled(true);
                String errorMsg = e.getMessage() != null ? e.getMessage() : "Login Failed";
                Toast.makeText(signin.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}