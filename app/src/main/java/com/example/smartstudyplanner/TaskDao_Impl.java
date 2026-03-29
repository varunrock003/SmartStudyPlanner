package com.example.smartstudyplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class TaskDao_Impl implements TaskDao {
    private final AppDatabase dbHelper;

    public TaskDao_Impl(AppDatabase dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void insert(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", task.title);
        values.put("subject", task.subject);
        values.put("deadline", task.deadline);
        values.put("isCompleted", task.isCompleted ? 1 : 0);
        db.insert("Task", null, values);
    }

    @Override
    public void update(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", task.title);
        values.put("subject", task.subject);
        values.put("deadline", task.deadline);
        values.put("isCompleted", task.isCompleted ? 1 : 0);
        db.update("Task", values, "id = ?", new String[]{String.valueOf(task.id)});
    }

    @Override
    public void delete(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Task", "id = ?", new String[]{String.valueOf(task.id)});
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Task", null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                task.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                task.subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
                task.deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline"));
                task.isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) == 1;
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @Override
    public Task getTaskById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Task", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Task task = new Task();
            task.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            task.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            task.subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
            task.deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline"));
            task.isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) == 1;
            cursor.close();
            return task;
        }
        if (cursor != null) cursor.close();
        return null;
    }
}