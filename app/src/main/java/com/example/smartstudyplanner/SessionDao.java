package com.example.smartstudyplanner;

import java.util.List;

public interface SessionDao {
    void insert(StudySession session);
    List<StudySession> getAllSessions();
}