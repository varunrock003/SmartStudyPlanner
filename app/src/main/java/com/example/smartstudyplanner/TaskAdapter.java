package com.example.smartstudyplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.*;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    List<Task> taskList;

    public TaskAdapter(List<Task> list) {
        this.taskList = list;
    }

    public void updateList(List<Task> newList) {
        this.taskList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subject, deadline;
        MaterialButton editBtn, deleteBtn, completeBtn;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.taskTitle);
            subject = v.findViewById(R.id.taskSubject);
            deadline = v.findViewById(R.id.taskDeadline);
            editBtn = v.findViewById(R.id.editBtn);
            deleteBtn = v.findViewById(R.id.deleteBtn);
            completeBtn = v.findViewById(R.id.completeBtn);
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

        // Strike through text if completed
        if (t.isCompleted) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.completeBtn.setIconResource(android.R.drawable.checkbox_on_background);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.completeBtn.setIconResource(android.R.drawable.checkbox_off_background);
        }

        holder.completeBtn.setOnClickListener(v -> {
            new Thread(() -> {
                t.isCompleted = !t.isCompleted;
                AppDatabase.getDatabase(v.getContext()).taskDao().update(t);
                ((DashboardActivity) v.getContext()).runOnUiThread(() -> {
                    notifyItemChanged(position);
                });
            }).start();
        });

        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddTaskActivity.class);
            intent.putExtra("TASK_ID", t.id);
            v.getContext().startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            new Thread(() -> {
                Context context = v.getContext();
                AppDatabase db = AppDatabase.getDatabase(context);
                db.taskDao().delete(t);

                // Get current user ID to fetch updated list correctly
                SharedPreferences sharedPref = context.getSharedPreferences("SmartStudyPref", Context.MODE_PRIVATE);
                int userId = sharedPref.getInt("USER_ID", -1);
                
                final List<Task> updatedList = db.taskDao().getAllTasksForUser(userId);
                ((DashboardActivity) context).runOnUiThread(() -> {
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
