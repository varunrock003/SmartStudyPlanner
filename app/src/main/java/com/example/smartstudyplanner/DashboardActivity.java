package com.example.smartstudyplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private Button addTaskBtn, timerBtn, analyticsBtn, darkBtn;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private MaterialButtonToggleGroup filterGroup;
    private List<Task> allTasks = new ArrayList<>();
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Get current user ID
        SharedPreferences sharedPref = getSharedPreferences("SmartStudyPref", Context.MODE_PRIVATE);
        currentUserId = sharedPref.getInt("USER_ID", -1);

        if (currentUserId == -1) {
            // Should not happen if login is working, but safety first
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db = AppDatabase.getDatabase(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        filterGroup = findViewById(R.id.filterToggleGroup);
        filterGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                applyFilter(checkedId);
            }
        });

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
        
        refreshTaskList();
    }

    private void applyFilter(int checkedId) {
        List<Task> filteredList = new ArrayList<>();
        if (checkedId == R.id.filterAll) {
            filteredList.addAll(allTasks);
        } else if (checkedId == R.id.filterPending) {
            for (Task t : allTasks) {
                if (!t.isCompleted) filteredList.add(t);
            }
        } else if (checkedId == R.id.filterCompleted) {
            for (Task t : allTasks) {
                if (t.isCompleted) filteredList.add(t);
            }
        }
        
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }

    private void refreshTaskList() {
        if (db != null && recyclerView != null) {
            new Thread(() -> {
                // Fetch tasks ONLY for the current user
                allTasks = db.taskDao().getAllTasksForUser(currentUserId);
                runOnUiThread(() -> {
                    if (adapter == null) {
                        adapter = new TaskAdapter(new ArrayList<>(allTasks));
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateList(new ArrayList<>(allTasks));
                    }
                    applyFilter(filterGroup.getCheckedButtonId());
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
