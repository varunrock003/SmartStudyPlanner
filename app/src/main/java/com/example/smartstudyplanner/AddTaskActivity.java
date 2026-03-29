package com.example.smartstudyplanner;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    EditText title, subject, deadline;
    Button saveBtn;
    TextView header;
    int taskId = -1;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        SharedPreferences sharedPref = getSharedPreferences("SmartStudyPref", Context.MODE_PRIVATE);
        currentUserId = sharedPref.getInt("USER_ID", -1);

        title = findViewById(R.id.title);
        subject = findViewById(R.id.subject);
        deadline = findViewById(R.id.deadline);
        saveBtn = findViewById(R.id.saveBtn);
        header = findViewById(R.id.header);

        // Calendar Date Picker setup
        deadline.setOnClickListener(v -> showDatePicker());
        deadline.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showDatePicker();
        });

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
                    task.userId = currentUserId; // Set the owner of the task
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

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    deadline.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
