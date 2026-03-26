package com.example.smartstudyplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StudySession {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public long timestamp;
    public long durationMinutes;
}