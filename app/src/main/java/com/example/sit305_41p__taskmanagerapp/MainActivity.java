package com.example.sit305_41p__taskmanagerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private EditText titleEditText, descriptionEditText;
    private TextView selectedDateTextView;
    private Button datePickerButton, createNewTaskButton;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        datePickerButton = findViewById(R.id.datePickerButton);
        createNewTaskButton = findViewById(R.id.createNewTask);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);

        // Setup RecyclerView
        taskAdapter = new TaskAdapter(taskList, this, this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        // Set up button click listeners
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());
        createNewTaskButton.setOnClickListener(v -> createNewTask());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this::onDateSet,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void onDateSet(DatePicker view, int year, int month, int day) {
        selectedDate.set(year, month, day);
        updateSelectedDateText();
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        selectedDateTextView.setText(sdf.format(selectedDate.getTime()));
    }

    private void createNewTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            showToast("Please enter a title");
            return;
        }

        if (selectedDateTextView.getText().toString().equals("No date selected")) {
            showToast("Please select a due date");
            return;
        }

        Task newTask = new Task(title, description, selectedDate.getTime());
        taskList.add(newTask);
        taskAdapter.notifyItemInserted(taskList.size() - 1);

        clearInputFields();
        showToast("Task created successfully");
    }

    private void clearInputFields() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        selectedDate = Calendar.getInstance();
        selectedDateTextView.setText("No date selected");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskClick(Task task) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String message = String.format("Task: %s\nDescription: %s\nDue: %s",
                task.getTitle(),
                task.getDescription(),
                sdf.format(task.getDueDate()));
        showToast(message);
    }

    @Override
    public void onDeleteClick(Task task) {
        int position = taskList.indexOf(task);
        taskList.remove(task);
        taskAdapter.notifyItemRemoved(position);
        showToast("Task deleted");
    }

    @Override
    public void onEditClick(Task task, int position) {
        showToast("Task updated");
    }

    public static class Task {
        private String title;
        private String description;
        private Date dueDate;

        public Task(String title, String description, Date dueDate) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Date getDueDate() { return dueDate; }

        public void setTitle(String title) { this.title = title; }
        public void setDescription(String description) { this.description = description; }
        public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    }
}