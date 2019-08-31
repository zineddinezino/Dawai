package com.sci.dawai;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email,phone,pass;
    Button signup;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.emailEt);
        phone = findViewById(R.id.phoneSignEt);
        pass = findViewById(R.id.passSignEt);
        signup = findViewById(R.id.signup_btn);
        progressBar = findViewById(R.id.progress_circular_signup);
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser()
    {
        String email_address = email.getText().toString().trim();
        String phone_number = phone.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if(email_address.isEmpty()){
            email.setError("The email is required");
            email.requestFocus();
            return;
        }else if(phone_number.isEmpty()){
            phone.setError("The phone number is required");
            phone.requestFocus();
            return;
        }else if(password.isEmpty()){
            pass.setError("The password is required");
            pass.requestFocus();
            return;
        }else if(password.length() < 6){
            pass.setError("Minimum length of password should be 6");
            pass.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email_address,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent i = new Intent(SignupActivity.this, ProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(SignupActivity.this, "You are already registered",  Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}
