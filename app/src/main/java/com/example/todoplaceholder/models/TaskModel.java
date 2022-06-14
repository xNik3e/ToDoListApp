package com.example.todoplaceholder.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todoplaceholder.utils.utils.PhotoHelper;
import com.github.javafaker.Faker;

import org.jetbrains.annotations.PropertyKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@Entity(tableName = "task_table")
public class TaskModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String taskName;

    private CategoryModel model;

    private String description;
    private Date notificationTime;
    private Date endDate;
    private boolean active;
    private Date createdAt;
    private int notificationUniqueID = -1;

    private List<String> attachedFileNames = new ArrayList<>();
    @Ignore
    private List<Bitmap> attachedFileBitmaps = new ArrayList<>();

    @Ignore
    private Date simpleDate;

    private void setNotifID() {
        if (this.notificationUniqueID == -1) {
            Faker faker = new Faker();
            this.notificationUniqueID = faker.random().nextInt(2000, 2147483000);
        }
    }


    @Ignore
    public TaskModel(String taskName) {
        this.taskName = taskName;
        this.model = new CategoryModel(taskName);
        this.description = null;
        this.notificationTime = null;
        this.active = true;
        this.createdAt = new Date(System.currentTimeMillis());
        setNotifID();
    }

    @Ignore
    public TaskModel(String taskName, CategoryModel model) {
        this.taskName = taskName;
        this.model = model;
        this.description = null;
        this.notificationTime = null;
        this.active = true;
        this.createdAt = new Date(System.currentTimeMillis());
        setNotifID();
    }

    @Ignore
    public TaskModel(String taskName, CategoryModel model, String description) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = null;
        this.active = true;
        this.createdAt = new Date(System.currentTimeMillis());
        setNotifID();
    }

    @Ignore
    public TaskModel(String taskName, CategoryModel model, String description, Date notificationTime) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.active = true;
        this.createdAt = new Date(System.currentTimeMillis());
        setNotifID();
    }

    @Ignore
    public TaskModel(@NonNull String taskName, CategoryModel model, String description, Date notificationTime, boolean active) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.active = active;
        this.createdAt = new Date(System.currentTimeMillis());
        setNotifID();
    }

    public TaskModel(String taskName, CategoryModel model, String description, Date notificationTime, Date endDate, boolean active, List<String> attachedFileNames, int notificationUniqueID) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.endDate = endDate;
        this.active = active;
        this.createdAt = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        this.simpleDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).getTime();
        this.attachedFileNames = attachedFileNames;
        attachedFileNames.forEach(fileName -> attachedFileBitmaps.add(PhotoHelper.loadImageFromStorage(fileName)));
        if(this.notificationUniqueID == -1){
            Faker faker = new Faker();
            this.notificationUniqueID = faker.random().nextInt(2000, 2147483000);
        }else
            this.notificationUniqueID = notificationUniqueID;
    }
    @Ignore
    public TaskModel(String taskName, CategoryModel model, String description, Date notificationTime, Date endDate, boolean active, List<String> attachedFileNames, List<Bitmap> bitmaps) {
        this.taskName = taskName;
        this.model = model;
        this.description = description;
        this.notificationTime = notificationTime;
        this.endDate = endDate;
        this.active = active;
        this.createdAt = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        this.simpleDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).getTime();
        this.attachedFileNames = attachedFileNames;
        this.attachedFileBitmaps = bitmaps;
        setNotifID();
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(Date simpleDate) {
        this.simpleDate = simpleDate;
    }

    public List<Bitmap> getAttachedFileBitmaps() {
        return attachedFileBitmaps;
    }

    public void setAttachedFileBitmaps(List<Bitmap> attachedFileBitmaps) {
        this.attachedFileBitmaps = attachedFileBitmaps;
    }

    public List<String> getAttachedFileNames() {
        return attachedFileNames;
    }

    public int getNotificationUniqueID() {
        return notificationUniqueID;
    }

    public void setNotificationUniqueID(int notificationUniqueID) {
        this.notificationUniqueID = notificationUniqueID;
    }

    public void setAttachedFileNames(List<String> attachedFileNames) {
        this.attachedFileNames = attachedFileNames;


    }
}
