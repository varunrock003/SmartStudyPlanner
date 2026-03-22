package com.example.smartstudyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private Button addTaskBtn, timerBtn, analyticsBtn, darkBtn;
    private AppDatabase db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = AppDatabase.getDatabase(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshTaskList();

        addTaskBtn = findViewById(R.id.addTaskBtn);
        timerBtn = findViewById(R.id.timerBtn);
        analyticsBtn = findViewById(R.id.analyticsBtn);
        darkBtn = findViewById(R.id.darkModeBtn);

        darkBtn.setOnClickListener(v -> {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        addTaskBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AddTaskActivity.class)));

        timerBtn.setOnClickListener(v ->
                startActivity(new Intent(this, StudyTimerActivity.class)));

        analyticsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, AnalyticsActivity.class)));
    }

    private void refreshTaskList() {
        if (db != null && recyclerView != null) {
            new Thread(() -> {
                List<Task> tasks = db.taskDao().getAllTasks();
                runOnUiThread(() -> {
                    TaskAdapter adapter = new TaskAdapter(tasks);
                    recyclerView.setAdapter(adapter);
                });
            }).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTaskList();
    }
}
