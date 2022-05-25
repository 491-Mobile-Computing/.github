package com.example.hometownapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class PhoneRegister extends AppCompatActivity {
    private static final String TAG = "PhoneVerification";
    private Button sButton;
    private EditText phoneNum;
    private TextView processText;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoneregister);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sButton = findViewById(R.id.SendCode);
        processText = findViewById(R.id.processStatus);
        phoneNum = findViewById(R.id.phoneNum);

        mAuth = FirebaseAuth.getInstance();

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = "+1" + "" + phoneNum.getText().toString();
                if(!phoneNum.getText().toString().isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNo)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(PhoneRegister.this)
                            .setCallbacks(mCallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber((options));
                }
                else{
                    processText.setText("Please enter the phone number");
                    processText.setVisibility(View.VISIBLE);
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                processText.setText("OTP has been sent");
                processText.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(PhoneRegister.this, Verify.class);
                        otpIntent.putExtra("auth", verificationId);
                        startActivity(otpIntent);
                    }
                }, 5000);

            }
        };
    }
   /* @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            sendToLogin();
        }
    }
    private void sendToLogin(){
        startActivity(new Intent(PhoneRegister.this, Login.class));
        finish();
    }*/
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            startActivity(new Intent(PhoneRegister.this, Login.class ));
                            finish();
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
    }
}
