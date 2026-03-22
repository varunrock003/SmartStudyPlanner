package com.example.smartstudyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText title, subject, deadline;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title = findViewById(R.id.title);
        subject = findViewById(R.id.subject);
        deadline = findViewById(R.id.deadline);
        saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            String taskTitle = title.getText().toString();
            String taskSubject = subject.getText().toString();
            String taskDeadline = deadline.getText().toString();

            if (taskTitle.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task();
            task.title = taskTitle;
            task.subject = taskSubject;
            task.deadline = taskDeadline;

            new Thread(() -> {
                AppDatabase.getDatabase(this).taskDao().insert(task);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Task Saved!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}