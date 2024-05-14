package com.example.oopproject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class Task {

    private String title;
    private boolean isCompleted;
    private Date creationDate;
    private Date completionDate;
    private String description;

    public Task(String title) {
        this.title = title;
        this.isCompleted = false; // New tasks are not completed by default
        this.creationDate = new Date();
        this.description = ""; // Initialize description as empty
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
        if (completed) {
            completionDate = new Date(); // Set the completion date when the task is marked as completed
        }
    }
}

