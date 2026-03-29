package com.example.smartstudyplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    TextView totalTime, tasksDone, burnoutStatus;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        SharedPreferences sharedPref = getSharedPreferences("SmartStudyPref", Context.MODE_PRIVATE);
        currentUserId = sharedPref.getInt("USER_ID", -1);

        totalTime = findViewById(R.id.totalTime);
        tasksDone = findViewById(R.id.tasksDone);
        burnoutStatus = findViewById(R.id.burnoutStatus);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            // Fetch only tasks and sessions belonging to the current user
            List<Task> tasks = db.taskDao().getAllTasksForUser(currentUserId);
            List<StudySession> sessions = db.sessionDao().getAllSessionsForUser(currentUserId);

            long totalMinutes = 0;
            for (StudySession session : sessions) {
                totalMinutes += session.durationMinutes;
            }

            final long finalMinutes = totalMinutes;
            runOnUiThread(() -> {
                tasksDone.setText("Tasks Created: " + tasks.size());
                totalTime.setText("Total Study Time: " + finalMinutes + " mins");
                
                updateBurnoutStatus(finalMinutes);
            });
        }).start();
    }

    private void updateBurnoutStatus(long totalMinutes) {
        if (totalMinutes > 240) { // More than 4 hours
            burnoutStatus.setText("Burnout Status: High Risk! Take a long break.");
            burnoutStatus.setTextColor(Color.RED);
        } else if (totalMinutes > 120) { // More than 2 hours
            burnoutStatus.setText("Burnout Status: Moderate. Consider a short break.");
            burnoutStatus.setTextColor(Color.parseColor("#FFA500")); // Orange
        } else {
            burnoutStatus.setText("Burnout Status: Low. Keep going!");
            burnoutStatus.setTextColor(Color.GREEN);
        }
    }
}
