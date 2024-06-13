package com.example.oopproject;

import java.util.Date; // Import Date for date handling


/*This class represents a task with properties such as title, description, creation date, completion date, and completion status.
It provides methods to get and set these properties.
The constructor initializes a new task with a given title, sets the creation date to the current date, and marks the task as not completed by default.
The task's completion status can be updated, and when marked as completed, the completion date is set to the current date.
 */

public class Task {

    private String title; // Title of the task
    private boolean isCompleted; // Status of task completion
    private Date creationDate; // Date when the task was created
    private Date completionDate; // Date when the task was completed
    private String description; // Description of the task

    public Task(String title) { // Constructor to initialize task with a title
        this.title = title; // Set the title
        this.isCompleted = false; // New tasks are not completed by default
        this.creationDate = new Date(); // Set the creation date to current date
        this.description = ""; // Initialize description as empty
    }

    public String getTitle() { // Get the title of the task
        return title;
    }

    public String getDescription() { // Get the description of the task
        return description;
    }

    public Date getCreationDate() { // Get the creation date of the task
        return creationDate;
    }

    public Date getCompletionDate() { // Get the completion date of the task
        return completionDate;
    }

    public boolean isCompleted() { // Check if the task is completed
        return isCompleted;
    }

    public void setDescription(String description) { // Set the description of the task
        this.description = description;
    }

    public void setCompleted(boolean completed) { // Set the task as completed or not
        isCompleted = completed; // Update the completion status
        if (completed) { // If task is marked as completed
            completionDate = new Date(); // Set the completion date to current date
        }
    }
}
