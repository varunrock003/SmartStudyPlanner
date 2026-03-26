package com.example.smartstudyplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText title, subject, deadline;
    Button saveBtn;
    TextView header;
    int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title = findViewById(R.id.title);
        subject = findViewById(R.id.subject);
        deadline = findViewById(R.id.deadline);
        saveBtn = findViewById(R.id.saveBtn);
        header = findViewById(R.id.header);

        taskId = getIntent().getIntExtra("TASK_ID", -1);

        if (taskId != -1) {
            header.setText("Edit Task");
            saveBtn.setText("Update Task");
            new Thread(() -> {
                Task task = AppDatabase.getDatabase(this).taskDao().getTaskById(taskId);
                if (task != null) {
                    runOnUiThread(() -> {
                        title.setText(task.title);
                        subject.setText(task.subject);
                        deadline.setText(task.deadline);
                    });
                }
            }).start();
        }

        saveBtn.setOnClickListener(v -> {
            String taskTitle = title.getText().toString();
            String taskSubject = subject.getText().toString();
            String taskDeadline = deadline.getText().toString();

            if (taskTitle.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                TaskDao taskDao = AppDatabase.getDatabase(this).taskDao();
                if (taskId == -1) {
                    Task task = new Task();
                    task.title = taskTitle;
                    task.subject = taskSubject;
                    task.deadline = taskDeadline;
                    taskDao.insert(task);
                } else {
                    Task task = taskDao.getTaskById(taskId);
                    if (task != null) {
                        task.title = taskTitle;
                        task.subject = taskSubject;
                        task.deadline = taskDeadline;
                        taskDao.update(task);
                    }
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, taskId == -1 ? "Task Saved!" : "Task Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}