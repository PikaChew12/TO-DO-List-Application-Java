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



    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public Task(String title) {
        this.title = title;
        this.isCompleted = false; // New tasks are not completed by default
        this.creationDate = new Date();
        this.description = ""; // Initialize description as empty
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public String getFormattedCreationDate() {
        return dateFormat.format(creationDate);
    }

    // Get formatted completion date
    public String getFormattedCompletionDate() {
        if (completionDate != null) {
            return dateFormat.format(completionDate);
        } else {
            return "Not completed";
        }
    }


    public Date getCompletionDate() {
        return completionDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
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

