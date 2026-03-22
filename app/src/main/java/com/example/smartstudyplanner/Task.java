package com.example.smartstudyplanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String subject;
    public String deadline;
    public boolean isCompleted;
}