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

    private List<MainActivity.Task> taskList;
    private OnTaskClickListener listener;

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
        MainActivity.Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dueDateTextView;
        private Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            dueDateTextView = itemView.findViewById(R.id.taskDueDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(final MainActivity.Task task, final OnTaskClickListener listener) {
            titleTextView.setText(task.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            dueDateTextView.setText(sdf.format(task.getDueDate()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTaskClick(task);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(task);
                }
            });
        }
    }
}