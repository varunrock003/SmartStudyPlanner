package com.example.smartstudyplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class SessionDao_Impl implements SessionDao {
    private final AppDatabase dbHelper;

    public SessionDao_Impl(AppDatabase dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void insert(StudySession session) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp", session.timestamp);
        db.insert("StudySession", null, values);
    }

    @Override
    public List<StudySession> getAllSessions() {
        List<StudySession> sessions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM StudySession", null);

        if (cursor.moveToFirst()) {
            do {
                StudySession session = new StudySession();
                session.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                session.timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
                sessions.add(session);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }
}