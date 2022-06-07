package com.example.todoplaceholder.models;


import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DateModel{
    private int day;
    private int month;
    private int year;
    private boolean isActive;
    private Date date;


    public DateModel(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
        this.date = new GregorianCalendar(this.year, this.month - 1, this.day).getTime();
    }

    public DateModel(int day, int month, int year) {
        this.day = day;
        this.month = month;
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

    public static List<DateModel> createDateList(List<TaskModel> taskModels){
        List<Date> dates = taskModels.stream()
                .map(TaskModel::getEndDate)
                .collect(Collectors.toList());

        List<DateModel> tempModels = new ArrayList<>();

        dates.stream()
                .map(DateModel::new)
                .forEach(tempModels::add);

        dates = tempModels.stream()
                .map(DateModel::getDate)
                .distinct().collect(Collectors.toList());

        tempModels.clear();
        dates.stream()
                .map(DateModel::new)
                .forEach(tempModels::add);

        tempModels.sort(Comparator.comparing(DateModel::getDate));

        return  tempModels;
    }



}
