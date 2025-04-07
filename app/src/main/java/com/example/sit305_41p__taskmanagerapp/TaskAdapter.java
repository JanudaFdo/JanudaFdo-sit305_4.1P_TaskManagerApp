package com.example.sit305_41p__taskmanagerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<MainActivity.Task> taskList;
    private final OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(MainActivity.Task task);
        void onDeleteClick(MainActivity.Task task);
    }

    public TaskAdapter(List<MainActivity.Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView dueDateTextView;
        private final TextView descriptionTextView;
        private final Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            dueDateTextView = itemView.findViewById(R.id.taskDueDate);
            descriptionTextView = itemView.findViewById(R.id.taskDescription);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(final MainActivity.Task task, final OnTaskClickListener listener) {
            titleTextView.setText(task.getTitle());

            // Format and display due date
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            dueDateTextView.setText(sdf.format(task.getDueDate()));

            // Display description (show "No description" if empty)
            String description = task.getDescription();
            descriptionTextView.setText(description.isEmpty() ? "No description" : description);

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(task));
        }
    }
}