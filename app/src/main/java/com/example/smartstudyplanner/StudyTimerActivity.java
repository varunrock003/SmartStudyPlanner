package com.example.smartstudyplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudyTimerActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startBtn, pauseBtn, resetBtn, increaseBtn, decreaseBtn;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis = 1500000; // Default 25 min
    private long initialTimeInMillis = 1500000;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_timer);

        SharedPreferences sharedPref = getSharedPreferences("SmartStudyPref", Context.MODE_PRIVATE);
        currentUserId = sharedPref.getInt("USER_ID", -1);

        timerText = findViewById(R.id.timerText);
        startBtn = findViewById(R.id.startBtn);
        pauseBtn = findViewById(R.id.pauseBtn);
        resetBtn = findViewById(R.id.resetBtn);
        increaseBtn = findViewById(R.id.increaseBtn);
        decreaseBtn = findViewById(R.id.decreaseBtn);

        startBtn.setOnClickListener(v -> startTimer());
        pauseBtn.setOnClickListener(v -> pauseTimer());
        resetBtn.setOnClickListener(v -> resetTimer());

        increaseBtn.setOnClickListener(v -> {
            if (!timerRunning) {
                timeLeftInMillis += 60000; // Add 1 minute
                initialTimeInMillis = timeLeftInMillis;
                updateCountDownText();
            }
        });

        decreaseBtn.setOnClickListener(v -> {
            if (!timerRunning && timeLeftInMillis > 60000) {
                timeLeftInMillis -= 60000; // Subtract 1 minute
                initialTimeInMillis = timeLeftInMillis;
                updateCountDownText();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
                increaseBtn.setEnabled(true);
                decreaseBtn.setEnabled(true);
                
                // Save session to database linked to the current user
                StudySession session = new StudySession();
                session.userId = currentUserId;
                session.timestamp = System.currentTimeMillis();
                session.durationMinutes = initialTimeInMillis / 60000;
                
                new Thread(() -> {
                    AppDatabase.getDatabase(getApplicationContext()).sessionDao().insert(session);
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),
                                "Session Done! Saved to Analytics.", Toast.LENGTH_LONG).show();
                        timeLeftInMillis = initialTimeInMillis; // Reset for next time
                        updateCountDownText();
                    });
                }).start();
            }
        }.start();

        timerRunning = true;
        startBtn.setVisibility(View.GONE);
        pauseBtn.setVisibility(View.VISIBLE);
        increaseBtn.setEnabled(false);
        decreaseBtn.setEnabled(false);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        startBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        timeLeftInMillis = initialTimeInMillis;
        updateCountDownText();
        startBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        increaseBtn.setEnabled(true);
        decreaseBtn.setEnabled(true);
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
