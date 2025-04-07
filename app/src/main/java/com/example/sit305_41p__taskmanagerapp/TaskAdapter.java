package com.example.sit305_41p__taskmanagerapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<MainActivity.Task> taskList;
    private final OnTaskClickListener listener;
    private final Context context;

    public interface OnTaskClickListener {
        void onTaskClick(MainActivity.Task task);
        void onDeleteClick(MainActivity.Task task);
        void onEditClick(MainActivity.Task task, int position);
    }

    public TaskAdapter(List<MainActivity.Task> taskList, OnTaskClickListener listener, Context context) {
        this.taskList = taskList;
        this.listener = listener;
        this.context = context;
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
        holder.bind(taskList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView dueDateTextView;
        private final TextView descriptionTextView;
        private final Button deleteButton;
        private final Button editButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            dueDateTextView = itemView.findViewById(R.id.taskDueDate);
            descriptionTextView = itemView.findViewById(R.id.taskDescription);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public void bind(final MainActivity.Task task, final OnTaskClickListener listener, int position) {
            titleTextView.setText(task.getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            dueDateTextView.setText(sdf.format(task.getDueDate()));

            String description = task.getDescription();
            descriptionTextView.setText(description.isEmpty() ? "No description" : description);

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(task));
            editButton.setOnClickListener(v -> showEditDialog(task, position));
        }

        private void showEditDialog(MainActivity.Task task, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null);
            builder.setView(dialogView);
            builder.setTitle("Edit Task");

            EditText editTitle = dialogView.findViewById(R.id.editTitle);
            EditText editDescription = dialogView.findViewById(R.id.editDescription);
            Button editDateButton = dialogView.findViewById(R.id.editDateButton);
            TextView editDateText = dialogView.findViewById(R.id.editDateText);

            editTitle.setText(task.getTitle());
            editDescription.setText(task.getDescription());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            editDateText.setText(sdf.format(task.getDueDate()));

            Calendar editCalendar = Calendar.getInstance();
            editCalendar.setTime(task.getDueDate());

            editDateButton.setOnClickListener(v -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        (view, year, month, dayOfMonth) -> {
                            editCalendar.set(year, month, dayOfMonth);
                            editDateText.setText(sdf.format(editCalendar.getTime()));
                        },
                        editCalendar.get(Calendar.YEAR),
                        editCalendar.get(Calendar.MONTH),
                        editCalendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            });

            builder.setPositiveButton("Save", (dialog, which) -> {
                String newTitle = editTitle.getText().toString().trim();
                String newDescription = editDescription.getText().toString().trim();

                if (newTitle.isEmpty()) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setDueDate(editCalendar.getTime());
                notifyItemChanged(position);
                listener.onEditClick(task, position);
            });

            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
    }
}