package com.example.todoplaceholder.models;


import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DateModel {
    private int day;
    private int month;
    private int year;
    private boolean isActive;
    private Date date;


    public DateModel(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.isActive = false;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        this.date = new GregorianCalendar(this.year, this.month - 1, this.day).getTime();
    }

    public DateModel(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.isActive = false;
        this.year = year;
        this.date = new GregorianCalendar(year, month - 1, day).getTime();
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static List<DateModel> createDateList(List<TaskModel> taskModels) {

        List<DateModel> tempModels = new ArrayList<>();

        taskModels.stream()
                .map(TaskModel::getEndDate)
                .map(DateModel::new)
                .map(DateModel::getDate)
                .distinct()
                .map(DateModel::new)
                .sorted(Comparator.comparing(DateModel::getDate))
                .forEach(tempModels::add);

        if (tempModels.size() > 0)
            tempModels.get(0).setActive(true);

        return tempModels;
    }
}
