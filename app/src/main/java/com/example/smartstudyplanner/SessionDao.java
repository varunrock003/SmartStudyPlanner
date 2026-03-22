package com.example.smartstudyplanner;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SessionDao {
    @Insert
    void insert(StudySession session);

    @Query("SELECT * FROM StudySession")
    List<StudySession> getAllSessions();
}