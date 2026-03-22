package com.example.smartstudyplanner;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudyTimerActivity extends AppCompatActivity {

    TextView timerText;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_timer);

        timerText = findViewById(R.id.timerText);
        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(v -> startTimer());
    }

    private void startTimer() {
        startBtn.setEnabled(false);
        new CountDownTimer(1500000, 1000) { // 25 min

            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                timerText.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                // Save session to database
                StudySession session = new StudySession();
                session.timestamp = System.currentTimeMillis();
                
                new Thread(() -> {
                    AppDatabase.getDatabase(getApplicationContext()).sessionDao().insert(session);
                    runOnUiThread(() -> {
                        startBtn.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                "Session Done! Saved to Analytics.", Toast.LENGTH_LONG).show();
                    });
                }).start();
            }
        }.start();
    }
}