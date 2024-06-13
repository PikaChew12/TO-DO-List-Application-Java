package com.example.oopproject;

import android.content.Context; // Import Context for accessing application-specific resources
import android.view.LayoutInflater; // Import LayoutInflater for inflating layout XML
import android.view.View; // Import View for UI components
import android.view.ViewGroup; // Import ViewGroup for container views
import android.widget.TextView; // Import TextView for displaying text
import android.widget.Button; // Import Button for button UI component
import androidx.recyclerview.widget.RecyclerView; // Import RecyclerView for displaying lists

import java.util.ArrayList; // Import ArrayList for list operations
import java.util.List; // Import List for list operations

/*This class is an adapter for a RecyclerView that displays a list of tasks.
It manages the creation and binding of view holders, which represent each task item.
The adapter handles user interactions such as clicking and long-clicking on tasks, as well as marking tasks as completed.
It provides methods to update the task list and retrieve the current list of tasks.
The adapter interfaces include OnTaskClickListener and OnTaskLongClickListener for handling task click events.
 */


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks; // List of tasks
    private Context mContext; // Context for accessing resources
    private OnTaskClickListener mListener; // Listener for task click events
    private OnTaskLongClickListener mLongListener; // Listener for task long click events

    public interface OnTaskClickListener { // Interface for task click listener
        void onTaskClick(Task task); // Method to be implemented for task click
    }

    public interface OnTaskLongClickListener { // Interface for task long click listener
        void onTaskLongClick(Task task); // Method to be implemented for task long click
    }

    public TaskAdapter(Context context, List<Task> tasks, OnTaskClickListener listener, OnTaskLongClickListener longClickListener) {
        mTasks = tasks; // Initialize tasks list
        mContext = context; // Initialize context
        mListener = listener; // Initialize task click listener
        mLongListener = longClickListener; // Initialize task long click listener
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle; // TextView for task title
        public TextView taskDescription; // TextView for task description
        public Button completeButton; // Button for marking task as complete

        public ViewHolder(View itemView) { // Constructor for ViewHolder
            super(itemView); // Call superclass constructor

            taskTitle = itemView.findViewById(R.id.taskTitle); // Find task title TextView by ID
            taskDescription = itemView.findViewById(R.id.taskDescription); // Find task description TextView by ID
            completeButton = itemView.findViewById(R.id.completeButton); // Find complete button by ID

            completeButton.setOnClickListener(v -> { // Set click listener for complete button
                int position = getAdapterPosition(); // Get adapter position
                if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                    Task task = mTasks.get(position); // Get task at position
                    if (task != null && !task.isCompleted()) { // Check if task is not null and not completed
                        TaskManager.getInstance(mContext).completeTask(task); // Mark task as complete in TaskManager
                        task.setCompleted(true); // Mark task as completed
                        notifyItemChanged(position); // Notify that item has changed
                    }
                }
            });

            itemView.setOnLongClickListener(v -> { // Set long click listener for item view
                int position = getAdapterPosition(); // Get adapter position
                if (position != RecyclerView.NO_POSITION && mLongListener != null) { // Check if position is valid and long click listener is not null
                    Task task = mTasks.get(position); // Get task at position
                    mLongListener.onTaskLongClick(task); // Trigger long click listener
                }
                return true; // Indicate the callback consumed the long click
            });

            itemView.setOnClickListener(v -> { // Set click listener for item view
                int position = getAdapterPosition(); // Get adapter position
                if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                    Task task = mTasks.get(position); // Get task at position
                    mListener.onTaskClick(task); // Trigger click listener
                }
            });
        }
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // Create new views (invoked by the layout manager)
        Context context = parent.getContext(); // Get context from parent
        LayoutInflater inflater = LayoutInflater.from(context); // Get LayoutInflater

        View taskView = inflater.inflate(R.layout.task_item, parent, false); // Inflate the custom layout
        return new ViewHolder(taskView); // Return a new holder instance
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder viewHolder, int position) { // Replace the contents of a view (invoked by the layout manager)
        Task task = mTasks.get(position); // Get task at position

        viewHolder.taskTitle.setText(task.getTitle()); // Set task title to TextView
        String description = task.getDescription(); // Get task description
        viewHolder.taskDescription.setText(description.contains("\n") ? description.substring(0, description.indexOf("\n")) : description); // Set task description to first line or full description

        viewHolder.completeButton.setVisibility(task.isCompleted() ? View.GONE : View.VISIBLE); // Set visibility of complete button based on task completion status
    }

    @Override
    public int getItemCount() { // Return the size of the dataset (invoked by the layout manager)
        return mTasks.size(); // Return the size of tasks list
    }

    public void updateTasks(List<Task> newTasks) { // Update tasks list and notify adapter
        System.out.println("Updating tasks. New size: " + newTasks.size()); // Debug: Print new tasks size
        for (Task task : newTasks) { // Debug: Print each task title and creation date
            System.out.println("Task: " + task.getTitle() + ", Date: " + task.getCreationDate());
        }
        mTasks.clear(); // Clear existing tasks
        mTasks.addAll(newTasks); // Add new tasks
        notifyDataSetChanged(); // Notify adapter of data change
    }

    public List<Task> getCurrentList() { // Get the current list of tasks
        System.out.println("Getting current list. Size: " + mTasks.size()); // Debug: Print current tasks size
        return new ArrayList<>(mTasks); // Return a copy of the current list to avoid modification issues
    }
}
