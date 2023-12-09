package com.example.androidlabs;

public class Todo {
    private int id;
    private String task;
    private boolean isUrgency;

    public Todo() {
        // Default constructor
    }

    public Todo(String task, boolean isUrgency) {
        this.task = task;
        this.isUrgency = isUrgency;
    }

    // Getters and setters for the attributes

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String Task) {
        this.task= Task;
    }

    public boolean isUrgency() {
        return isUrgency;
    }

    public void setUrgency(boolean urgency) {
        this.isUrgency = urgency;
    }
}
