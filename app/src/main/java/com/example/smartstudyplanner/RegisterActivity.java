package com.example.smartstudyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText nameField, emailField, passwordField;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(this);
                    User existingUser = db.userDao().getUserByEmail(email);
                    
                    if (existingUser != null) {
                        runOnUiThread(() -> Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show());
                    } else {
                        User user = new User();
                        user.name = name;
                        user.email = email;
                        user.password = password;
                        db.userDao().insert(user);
                        
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                }).start();
            }
        });
    }
}
