package com.example.smartstudyplanner;

import android.content.Intent;
import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    List<Task> taskList;

    public TaskAdapter(List<Task> list) {
        this.taskList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject, deadline;
        ImageButton editBtn, deleteBtn;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.taskTitle);
            subject = v.findViewById(R.id.taskSubject);
            deadline = v.findViewById(R.id.taskDeadline);
            editBtn = v.findViewById(R.id.editBtn);
            deleteBtn = v.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.title.setText(t.title);
        holder.subject.setText(t.subject);
        holder.deadline.setText(t.deadline);

        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddTaskActivity.class);
            intent.putExtra("TASK_ID", t.id);
            v.getContext().startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase.getDatabase(v.getContext()).taskDao().delete(t);
                List<Task> updatedList = AppDatabase.getDatabase(v.getContext()).taskDao().getAllTasks();
                ((DashboardActivity) v.getContext()).runOnUiThread(() -> {
                    taskList = updatedList;
                    notifyDataSetChanged();
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}