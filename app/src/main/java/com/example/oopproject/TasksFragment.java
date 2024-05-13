package com.example.oopproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksFragment extends Fragment implements TaskAdapter.OnTaskClickListener, TaskAdapter.OnTaskLongClickListener {

    private boolean showCompleted;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            showCompleted = getArguments().getBoolean("showCompleted");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>(), this, this);
        recyclerView.setAdapter(taskAdapter);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(getContext(), new ArrayList<>(), this, this);
        recyclerView.setAdapter(taskAdapter);
        observeTasks();
    }

    private void observeTasks() {
        TaskManager taskManager = TaskManager.getInstance(getContext());
        if (showCompleted) {
            taskManager.getCompletedTasksLiveData().observe(getViewLifecycleOwner(), this::updateTaskList);
        } else {
            taskManager.getActiveTasksLiveData().observe(getViewLifecycleOwner(), this::updateTaskList);
        }
    }

    public void updateTaskList(List<Task> tasks) {
        if (taskAdapter != null) {
            taskAdapter.updateTasks(tasks);
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTaskClick(Task task) {
        showTaskDetails(task);
    }

    @Override
    public void onTaskLongClick(Task task) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    TaskManager.getInstance(getContext()).removeTask(task);
                    taskAdapter.notifyDataSetChanged(); // Refresh the adapter
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_tasks, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_date) {
            sortTasksByDate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortTasksByDate() {
        List<Task> modifiableList = new ArrayList<>(taskAdapter.getCurrentList()); // Use the current list from adapter
        Collections.sort(modifiableList, (task1, task2) -> {
            Date date1 = task1.getCreationDate();
            Date date2 = task2.getCreationDate();
            return date1.compareTo(date2);
        });
        taskAdapter.updateTasks(modifiableList);
        taskAdapter.notifyDataSetChanged();
    }
}
