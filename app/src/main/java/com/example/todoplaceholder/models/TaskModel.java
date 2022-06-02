package com.example.todoplaceholder.models;

import java.util.Date;



public class TaskModel {

    private String taskName;
    private CategoryModel model;
    private String description;
    private Date notificationTime;

    public TaskModel(String taskName) {
        this.taskName = taskName;
        this.model = new CategoryModel(taskName);
        this.description = null;
        this.notificationTime = null;
    }

    public TaskModel(String taskName, CategoryModel model) {
        this.taskName = taskName;
        this.model = model;
        this.description = null;
        this.notificationTime = null;
    }

    public TaskModel(String taskName, CategoryModel model, String description) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = null;
    }

    public TaskModel(String taskName, CategoryModel model, String description, Date notificationTime) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public CategoryModel getModel() {
        return model;
    }

    public void setModel(CategoryModel model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }
}
