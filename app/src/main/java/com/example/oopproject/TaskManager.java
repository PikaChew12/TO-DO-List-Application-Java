package com.example.oopproject;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager {
    private static TaskManager instance; // Singleton instance
    private final List<Task> tasks; // List to hold all tasks
    private static Context appContext;

    private MutableLiveData<List<Task>> allTasksLiveData;
    private MutableLiveData<List<Task>> completedTasksLiveData;
    private MutableLiveData<List<Task>> activeTasksLiveData;

    private Gson gson = new Gson();
    private File file;
    private TaskManager() {
        tasks = Collections.synchronizedList(new ArrayList<>()); // Make tasks list thread-safe
        allTasksLiveData = new MutableLiveData<>();
        completedTasksLiveData = new MutableLiveData<>();
        activeTasksLiveData = new MutableLiveData<>();
        loadTasks();
        updateLiveDatas(); // Update all LiveData with initial task list
    }


    // Public method to get the singleton instance of TaskManager
    public static synchronized TaskManager getInstance(Context context) {
        if (instance == null) {
            appContext = context.getApplicationContext();
            instance = new TaskManager();
        }
        return instance;
    }
    private synchronized void saveTasks() {
        try (FileWriter writer = new FileWriter(getTasksFile())) {
            gson.toJson(tasks, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private synchronized void loadTasks() {
        file = getTasksFile();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type taskListType = new TypeToken<List<Task>>() {}.getType();
                List<Task> loadedTasks = gson.fromJson(reader, taskListType);
                if (loadedTasks != null) {
                    tasks.addAll(loadedTasks);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private File getTasksFile() {
        if (file == null) {
            file = new File(appContext.getFilesDir(), "tasks.json");
        }
        return file;
    }

    public LiveData<List<Task>> getAllTasksLiveData() {
        return allTasksLiveData;
    }

    public LiveData<List<Task>> getCompletedTasksLiveData() {
        return completedTasksLiveData;
    }

    public LiveData<List<Task>> getActiveTasksLiveData() {
        return activeTasksLiveData;
    }

    // Add a new task and sort the list if needed
    public synchronized void addTask(Task task) {
        tasks.add(task);
        updateLiveDatas();
        saveTasks();
    }

    // Remove a task
    public synchronized void removeTask(Task task) {
        tasks.remove(task);
        updateLiveDatas();
        saveTasks();
    }

    // Mark a task as completed
    public synchronized void completeTask(Task task) {
        if (tasks.contains(task)) {
            task.setCompleted(true);
            updateLiveDatas();
            saveTasks();
        }
    }

    // Get all tasks, providing an unmodifiable view of the list to prevent external modification
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
    }

    // Get only completed tasks
    public List<Task> getCompletedTasks() {
        return getTasksByCompletionStatus(true);
    }

    // Get only active tasks
    public List<Task> getActiveTasks() {
        return getTasksByCompletionStatus(false);
    }

    private void updateLiveDatas() {
        List<Task> activeTasks = new ArrayList<>();
        List<Task> completedTasks = new ArrayList<>();
        List<Task> allTasksCopy = new ArrayList<>(tasks);

        for (Task task : tasks) {
            if (task.isCompleted()) {
                completedTasks.add(task);
            } else {
                activeTasks.add(task);
            }
        }

        allTasksLiveData.postValue(allTasksCopy);
        completedTasksLiveData.postValue(completedTasks);
        activeTasksLiveData.postValue(activeTasks);
    }

    // Utility method to filter tasks by completion status
    private List<Task> getTasksByCompletionStatus(boolean isCompleted) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == isCompleted) {
                filteredTasks.add(task);
            }
        }
        return filteredTasks;
    }
    public synchronized void UpdateLiveDatas() {
        updateLiveDatas();
    }
}
