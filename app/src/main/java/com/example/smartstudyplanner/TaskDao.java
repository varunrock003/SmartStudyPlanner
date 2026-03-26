package com.example.smartstudyplanner;

import java.util.List;

public interface TaskDao {
    void insert(Task task);
    void update(Task task);
    void delete(Task task);
    List<Task> getAllTasks();
    Task getTaskById(int id);
}