package com.example.smartstudyplanner;

import android.view.*;
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

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.taskTitle);
            subject = v.findViewById(R.id.taskSubject);
            deadline = v.findViewById(R.id.taskDeadline);
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
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}