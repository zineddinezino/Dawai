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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    TextView create_account;
    private FirebaseAuth mAuth;
    EditText emailOrphone,pass;
    Button login;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        create_account = findViewById(R.id.create_accountTv);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        emailOrphone = findViewById(R.id.phoneLoginET);
        pass = findViewById(R.id.passLoginET);
        login = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_circular_login);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin(){
        String EmailorPhone = emailOrphone.getText().toString().trim();

        String password = pass.getText().toString().trim();

        if(EmailorPhone.isEmpty()){
            emailOrphone.setError("The email is required");
            emailOrphone.requestFocus();
            return;
        }else if(password.isEmpty()){
            pass.setError("The password is required");
            pass.requestFocus();
            return;
        }else if(password.length() < 6){
            pass.setError("Minimum length of password should be 6");
            pass.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(EmailorPhone).matches()){
            emailOrphone.setError("Please enter a valid email");
            emailOrphone.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(EmailorPhone,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
