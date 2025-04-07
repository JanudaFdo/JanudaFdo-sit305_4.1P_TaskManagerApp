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

public class MainActivity extends AppCompatActivity {

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
        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                // Handle task click (edit/view)
                showTaskDetails(task);
            }

            @Override
            public void onDeleteClick(Task task) {
                // Handle delete
                deleteTask(task);
            }
        });

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        // Date picker button click listener
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Create new task button click listener
        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, dayOfMonth);
                        updateSelectedDateText();
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateSelectedDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        selectedDateTextView.setText(sdf.format(selectedDate.getTime()));
    }

    private void createNewTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDateTextView.getText().toString().equals("No date selected")) {
            Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new task
        Task newTask = new Task(
                title,
                description,
                selectedDate.getTime()
        );

        // Add to list and update RecyclerView
        taskList.add(newTask);
        taskAdapter.notifyDataSetChanged();

        // Clear input fields
        titleEditText.setText("");
        descriptionEditText.setText("");
        selectedDate = Calendar.getInstance();
        selectedDateTextView.setText("No date selected");

        Toast.makeText(this, "Task created successfully", Toast.LENGTH_SHORT).show();
    }

    private void showTaskDetails(Task task) {
        // In a real app, you might open a new activity or show a dialog
        Toast.makeText(this,
                "Task: " + task.getTitle() +
                        "\nDue: " + new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(task.getDueDate()),
                Toast.LENGTH_LONG).show();
    }

    private void deleteTask(Task task) {
        taskList.remove(task);
        taskAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }

    // Task model class
    public static class Task {
        private String title;
        private String description;
        private Date dueDate;

        public Task(String title, String description, Date dueDate) {
            this.title = title;
            this.description = description;
            this.dueDate = dueDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Date getDueDate() {
            return dueDate;
        }
    }
}