package com.example.hometownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    //views
    private EditText rEmail, rPassword, rUsername;
    private Button bregister, bconfirm;

    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

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


        // Initialize value
        rEmail = findViewById(R.id.editTextTextEmailAddress);
        rPassword = findViewById(R.id.editTextTextPassword2);
        rUsername = findViewById(R.id.editTextTextPersonUsername);

        // Initialize button
        bregister = findViewById(R.id.buttonregister);
        bconfirm = findViewById(R.id.buttonconfirm);

        // Initialize phone



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");

       /* mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), "Verification code error", Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                super.onCodeSent(verificationId, token);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(Register.this, Verify.class);
                        otpIntent.putExtra("auth", verificationId);
                        startActivity(otpIntent);
                    }
                }, 10000);

            }
        };*/
            //handle register btn click
        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input email, password, username
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String username = rUsername.getText().toString().trim();

                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    rEmail.setError("Invalid Email");
                    rEmail.setFocusable(true);
                } else if (password.length() < 6) {
                    rPassword.setError("Password must be longer than 6 characters");
                    rPassword.setFocusable(true);
                } else {
                    registerUser(email, password);
                }
                /* get the phone number
                String phoneNo = "+1" + "" + phone.getText().toString();
                if(!phone.getText().toString().isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNo)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(Register.this)
                            .setCallbacks(mCallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber((options));
                }
                startActivity(new Intent(Register.this, Verify.class));*/


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
                                    startActivity(new Intent(Register.this, PhoneRegister.class));
                                    finish();
                                }else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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

    /*private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            startActivity(new Intent(Register.this, Login.class ));
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            Toast.makeText(getApplicationContext(), "Sign in code error",Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }*/
}

