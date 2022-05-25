package com.example.hometownapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity<onClick> extends AppCompatActivity implements View.OnClickListener {

    // private views
    private Button login, register;
    private EditText PersonName, Password;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action bar
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // init views

        PersonName = (EditText) findViewById(R.id.editTextTextPersonName);
        Password = (EditText) findViewById(R.id.editTextTextPassword);

        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(this);

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Register button click
            case R.id.register_btn:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.login_btn:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String username = PersonName.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (username.isEmpty()) {
            PersonName.setError("Email is required");
            Password.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            PersonName.setError("Enter a valid email");
            Password.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError("Password is required");
            Password.requestFocus();

        }

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if the user has been logged in
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();

                    //Get user email and uid from auth
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //Store data using Hashmap
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", email);
                    hashMap.put("uid", uid);
                    hashMap.put("name", "");
                    hashMap.put("phone", "");
                    hashMap.put("image", "");

                    //firebase database instance
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //path to store user data named "Users"
                    DatabaseReference reference = database.getReference("Users");
                    //put data withing hashmap in database
                    reference.child(uid).setValue(hashMap);
                    //user is logged in, start activity
                    startActivity(new Intent(MainActivity.this, Profile.class));

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
