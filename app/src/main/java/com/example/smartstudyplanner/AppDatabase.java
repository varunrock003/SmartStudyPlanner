package com.example.smartstudyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "study-db";
    private static final int DATABASE_VERSION = 6; // Incremented for task-user linking

    private static AppDatabase INSTANCE;
    private final TaskDao taskDao;
    private final SessionDao sessionDao;
    private final UserDao userDao;

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.taskDao = new TaskDao_Impl(this);
        this.sessionDao = new SessionDao_Impl(this);
        this.userDao = new UserDao_Impl(this);
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

    public UserDao userDao() {
        return userDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Task (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, title TEXT, subject TEXT, deadline TEXT, isCompleted INTEGER, FOREIGN KEY(userId) REFERENCES User(id))");
        db.execSQL("CREATE TABLE IF NOT EXISTS StudySession (id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, timestamp INTEGER, durationMinutes INTEGER, FOREIGN KEY(userId) REFERENCES User(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            db.execSQL("DROP TABLE IF EXISTS Task");
            db.execSQL("DROP TABLE IF EXISTS StudySession");
            onCreate(db);
        }
    }
}
