package com.example.smartstudyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "study-db";
    private static final int DATABASE_VERSION = 4; // Incremented version to ensure clean state and matches implementation

    private static AppDatabase INSTANCE;
    private final TaskDao taskDao;
    private final SessionDao sessionDao;

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.taskDao = new TaskDao_Impl(this);
        this.sessionDao = new SessionDao_Impl(this);
    }

    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppDatabase(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public TaskDao taskDao() {
        return taskDao;
    }

    public SessionDao sessionDao() {
        return sessionDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Task (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subject TEXT, deadline TEXT, isCompleted INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS StudySession (id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp INTEGER, durationMinutes INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Task");
        db.execSQL("DROP TABLE IF EXISTS StudySession");
        onCreate(db);
    }
}
