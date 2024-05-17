package com.example.oopproject;

import android.os.Bundle; // Used to pass data between Android components and save the state of the Fragment
import android.view.LayoutInflater; // Used to inflate XML layout files
import android.view.Menu; // Provides access to options menu
import android.view.MenuInflater; // Used to inflate the options menu
import android.view.MenuItem; // Represents an item in the options menu
import android.view.View; // Basic building block for user interface components
import android.view.ViewGroup; // Defines the layout parameters and the behavior of a View

import androidx.annotation.NonNull; // Denotes that a parameter or return value can never be null
import androidx.annotation.Nullable; // Denotes that a parameter or return value can be null
import androidx.fragment.app.Fragment; // Base class for creating Fragments
import androidx.recyclerview.widget.LinearLayoutManager; // Provides a layout manager for RecyclerView to arrange items in a linear fashion
import androidx.recyclerview.widget.RecyclerView; // Provides a flexible view for displaying a collection of items

import android.app.AlertDialog; // Used to build and show alert dialogs

import java.text.SimpleDateFormat; // Used for formatting and parsing dates in a locale-sensitive manner
import java.util.ArrayList; // Resizable array implementation of the List interface
import java.util.Collections; // Utility class that consists exclusively of static methods that operate on or return collections
import java.util.Comparator; // Comparison function, which imposes a total ordering on some collection of objects
import java.util.Date; // Represents a specific instant in time, with millisecond precision
import java.util.List; // Interface for a collection which can contain duplicate elements
import java.util.Locale; // A class that represents a specific geographical, political, or cultural region

public class TasksFragment extends Fragment implements TaskAdapter.OnTaskClickListener, TaskAdapter.OnTaskLongClickListener {

    private boolean showCompleted; // Flag to indicate whether to show completed tasks
    private TaskAdapter taskAdapter; // Adapter for RecyclerView
    private RecyclerView recyclerView; // RecyclerView to display tasks

    // Factory method to create a new instance of this fragment
    public static TasksFragment newInstance(boolean showCompleted) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putBoolean("showCompleted", showCompleted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu in this fragment
        if (getArguments() != null) {
            showCompleted = getArguments().getBoolean("showCompleted"); // Retrieve the showCompleted argument
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false); // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.tasksRecyclerView); // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>(), this, this); // Initialize adapter
        recyclerView.setAdapter(taskAdapter); // Set adapter to RecyclerView
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.tasksRecyclerView); // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>(), this, this); // Initialize adapter
        recyclerView.setAdapter(taskAdapter); // Set adapter to RecyclerView
        observeTasks(); // Observe task data changes
    }

    // Observe changes in task data
    private void observeTasks() {
        TaskManager taskManager = TaskManager.getInstance(getContext()); // Get TaskManager instance
        if (showCompleted) {
            taskManager.getCompletedTasksLiveData().observe(getViewLifecycleOwner(), this::updateTaskList); // Observe completed tasks
        } else {
            taskManager.getActiveTasksLiveData().observe(getViewLifecycleOwner(), this::updateTaskList); // Observe active tasks
        }
    }

    // Update the task list in the adapter
    public void updateTaskList(List<Task> tasks) {
        if (taskAdapter != null) {
            taskAdapter.updateTasks(tasks); // Update adapter with new task list
            taskAdapter.notifyDataSetChanged(); // Notify adapter of data changes
        }
    }

    // Handle task click events
    @Override
    public void onTaskClick(Task task) {
        showTaskDetails(task); // Show task details in a dialog
    }

    // Handle task long click events
    @Override
    public void onTaskLongClick(Task task) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    TaskManager.getInstance(getContext()).removeTask(task); // Remove task
                    taskAdapter.notifyDataSetChanged(); // Refresh the adapter
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Show task details in a dialog
    private void showTaskDetails(Task task) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String message = "Description: " + task.getDescription() +
                "\nCreation Date: " + dateFormat.format(task.getCreationDate()) +
                (task.isCompleted() ? "\nCompletion Date: " + dateFormat.format(task.getCompletionDate()) : "");

        new AlertDialog.Builder(getContext())
                .setTitle(task.getTitle())
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Create options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); // Clear existing menu items
        inflater.inflate(R.menu.menu_tasks, menu); // Inflate the menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Handle menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_date) {
            sortTasksByDate(); // Sort tasks by date
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Sort tasks by creation date in descending order
    private void sortTasksByDate() {
        List<Task> modifiableList = new ArrayList<>(taskAdapter.getCurrentList()); // Copy current list from adapter
        Collections.sort(modifiableList, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                Date date1 = task1.getCreationDate();
                Date date2 = task2.getCreationDate();
                return date2.compareTo(date1); // Reverse the order here by switching task1 and task2
            }
        });
        taskAdapter.updateTasks(modifiableList); // Update adapter with sorted list
        taskAdapter.notifyDataSetChanged(); // Notify adapter of data changes
    }
}
