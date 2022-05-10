package com.example.hometownapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    // private views
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide the action bar
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // init views
        register = (Button)findViewById(R.id.register_btn);
        login = (Button) findViewById(R.id.login_btn);

        // button click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //begin register
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

    }
}

