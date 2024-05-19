package com.example.oopproject;

import android.content.Context; // Used to get the application context for file operations

import androidx.lifecycle.LiveData; // Lifecycle-aware observable data holder class
import androidx.lifecycle.MutableLiveData; // Subclass of LiveData which is mutable

import com.google.gson.Gson; // Library for converting Java objects to JSON and vice versa
import com.google.gson.reflect.TypeToken; // Gson utility class for working with generic types

import java.io.File; // Class representing file and directory pathnames
import java.io.FileReader; // Class for reading character files
import java.io.FileWriter; // Class for writing character files
import java.lang.reflect.Type; // Class representing generic types

import java.util.ArrayList; // Resizable array implementation of the List interface
import java.util.Collections; // Utility class for collection operations
import java.util.List; // Interface for a collection which can contain duplicate elements

public class TaskManager {
    private static TaskManager instance; // Singleton instance
    private final List<Task> tasks; // List to hold all tasks
    private static Context appContext;

    private MutableLiveData<List<Task>> allTasksLiveData; // LiveData for all tasks
    private MutableLiveData<List<Task>> completedTasksLiveData; // LiveData for completed tasks
    private MutableLiveData<List<Task>> activeTasksLiveData; // LiveData for active tasks

    private Gson gson = new Gson(); // Gson for JSON serialization/deserialization
    private File file; // File to store tasks

    private TaskManager() {
        tasks = Collections.synchronizedList(new ArrayList<>()); // Make tasks list thread-safe
        allTasksLiveData = new MutableLiveData<>(); // Initialize LiveData
        completedTasksLiveData = new MutableLiveData<>(); // Initialize LiveData
        activeTasksLiveData = new MutableLiveData<>(); // Initialize LiveData
        loadTasks(); // Load tasks from file
        updateLiveDatas(); // Update all LiveData with initial task list
    }

    // Public method to get the singleton instance of TaskManager
    public static synchronized TaskManager getInstance(Context context) {
        if (instance == null) {
            appContext = context.getApplicationContext(); // Store application context
            instance = new TaskManager(); // Create new instance
        }
        return instance; // Return singleton instance
    }

    // Save tasks to file
    private synchronized void saveTasks() {
        try (FileWriter writer = new FileWriter(getTasksFile())) {
            gson.toJson(tasks, writer); // Serialize tasks to JSON and write to file
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace on exception
        }
    }

    // Load tasks from file
    private synchronized void loadTasks() {
        file = getTasksFile(); // Get tasks file
        if (file.exists()) { // Check if file exists
            try (FileReader reader = new FileReader(file)) {
                Type taskListType = new TypeToken<List<Task>>() {}.getType(); // Define type for deserialization
                List<Task> loadedTasks = gson.fromJson(reader, taskListType); // Deserialize tasks from JSON
                if (loadedTasks != null) {
                    tasks.addAll(loadedTasks); // Add loaded tasks to the list
                }
            } catch (Exception e) {
                e.printStackTrace(); // Print stack trace on exception
            }
        }
    }

    // Get the file to store tasks
    private File getTasksFile() {
        if (file == null) {
            file = new File(appContext.getFilesDir(), "tasks.json"); // Initialize file if null
        }
        return file; // Return file
    }

    // Get LiveData for completed tasks
    public LiveData<List<Task>> getCompletedTasksLiveData() {
        return completedTasksLiveData;
    }

    // Get LiveData for active tasks
    public LiveData<List<Task>> getActiveTasksLiveData() {
        return activeTasksLiveData;
    }

    // Add a new task
    public synchronized void addTask(Task task) {
        tasks.add(task); // Add task to list
        updateLiveDatas(); // Update LiveData
        saveTasks(); // Save tasks to file
    }

    // Remove a task
    public synchronized void removeTask(Task task) {
        tasks.remove(task); // Remove task from list
        updateLiveDatas(); // Update LiveData
        saveTasks(); // Save tasks to file
    }

    // Mark a task as completed
    public synchronized void completeTask(Task task) {
        if (tasks.contains(task)) { // Check if task is in list
            task.setCompleted(true); // Mark task as completed
            updateLiveDatas(); // Update LiveData
            saveTasks(); // Save tasks to file
        }
    }

    // Update LiveData for all, completed, and active tasks
    private void updateLiveDatas() {
        List<Task> activeTasks = new ArrayList<>(); // List for active tasks
        List<Task> completedTasks = new ArrayList<>(); // List for completed tasks
        List<Task> allTasksCopy = new ArrayList<>(tasks); // Copy of all tasks

        for (Task task : tasks) { // Iterate through tasks
            if (task.isCompleted()) {
                completedTasks.add(task); // Add to completed tasks
            } else {
                activeTasks.add(task); // Add to active tasks
            }
        }

        allTasksLiveData.postValue(allTasksCopy); // Update all tasks LiveData
        completedTasksLiveData.postValue(completedTasks); // Update completed tasks LiveData
        activeTasksLiveData.postValue(activeTasks); // Update active tasks LiveData
    }

    // Public method to update LiveData
    public synchronized void UpdateLiveDatas() {
        updateLiveDatas();
    }
}
