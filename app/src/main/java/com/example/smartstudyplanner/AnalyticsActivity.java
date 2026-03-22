package com.example.smartstudyplanner;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {

    TextView totalTime, tasksDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        totalTime = findViewById(R.id.totalTime);
        tasksDone = findViewById(R.id.tasksDone);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            List<Task> tasks = db.taskDao().getAllTasks();
            List<StudySession> sessions = db.sessionDao().getAllSessions();

            runOnUiThread(() -> {
                tasksDone.setText("Tasks Created: " + tasks.size());
                
                // 25 minutes per session
                long totalMinutes = sessions.size() * 25;
                totalTime.setText("Total Study Time: " + totalMinutes + " mins");
            });
        }).start();
    }
}