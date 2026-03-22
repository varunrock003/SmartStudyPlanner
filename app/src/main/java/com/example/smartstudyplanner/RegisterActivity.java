package com.example.smartstudyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}