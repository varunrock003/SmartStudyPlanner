package com.example.smartstudyplanner;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    TextView totalTime, tasksDone, burnoutStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        totalTime = findViewById(R.id.totalTime);
        tasksDone = findViewById(R.id.tasksDone);
        burnoutStatus = findViewById(R.id.burnoutStatus);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Task> tasks = db.taskDao().getAllTasks();
            List<StudySession> sessions = db.sessionDao().getAllSessions();

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