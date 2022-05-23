package com.example.hometownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    //views
    private EditText rEmail, rPassword, rUsername;
    private Button bregister, bconfirm;

    private FirebaseAuth mAuth;
    //progress bar
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide the action bar
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Action bar
        //ActionBar actionBar = getSupportActionBar();
       // actionBar.setTitle("Create account");

        //enable back button
      //  actionBar.setDisplayHomeAsUpEnabled(true);
      //  actionBar.setDisplayShowHomeEnabled(true);

        // Initialize value
        rEmail = findViewById(R.id.editTextTextEmailAddress);
        rPassword = findViewById(R.id.editTextTextPassword2);
        rUsername = findViewById(R.id.editTextTextPersonUsername);

        // Initialize button
        bregister = findViewById(R.id.buttonregister);
        bconfirm = findViewById(R.id.buttonconfirm);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");

        //handle register btn click
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input email, password, username
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String username = rUsername.getText().toString().trim();

                //validate input
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    rEmail.setError("Please provide valid email");
                    rEmail.requestFocus();

                } else if(email.isEmpty()){
                    rEmail.setError("Email is required");
                    rEmail.requestFocus();
                    return;
                }

                else if(username.isEmpty()){
                    rUsername.setError("Username is required");
                    rUsername.requestFocus();
                    return;
                }

                else if(password.isEmpty()){
                    rPassword.setError("Password is required");
                    rPassword.requestFocus();
                    return;
                }

                else if (password.length() < 6) {
                    rPassword.setError("Password must be longer than 6 characters");
                    rPassword.requestFocus();

                } else {
                    registerUser(email, password);
                }
            }
        });
    }
            private void registerUser(String email, String password) {

                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Register.this, "Registered\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Register.this, Profile.class));
                                    finish();
                                }else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed. Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}





