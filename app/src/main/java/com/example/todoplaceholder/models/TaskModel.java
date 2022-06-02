package com.example.todoplaceholder.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.PropertyKey;

import java.util.Date;


@Entity(tableName = "task_table")
public class TaskModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "taskName")
    private String taskName;
    @ColumnInfo(name = "model")
    private CategoryModel model;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "notificationTime")
    private Date notificationTime;
    @ColumnInfo(name ="active")
    private boolean active;



    @Ignore
    public TaskModel(String taskName) {
        this.taskName = taskName;
        this.model = new CategoryModel(taskName);
        this.description = null;
        this.notificationTime = null;
        this.active = true;
    }
    @Ignore
    public TaskModel(String taskName, CategoryModel model) {
        this.taskName = taskName;
        this.model = model;
        this.description = null;
        this.notificationTime = null;
        this.active = true;
    }
    @Ignore
    public TaskModel(String taskName, CategoryModel model, String description) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = null;
        this.active = true;
    }
    @Ignore
    public TaskModel(String taskName, CategoryModel model, String description, Date notificationTime) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.active = true;
    }

    public TaskModel(@NonNull String taskName, CategoryModel model, String description, Date notificationTime, boolean active) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.active = active;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
