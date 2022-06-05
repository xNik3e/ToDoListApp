package com.example.todoplaceholder.models;


import androidx.room.Entity;
import androidx.room.Ignore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateModel {
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
        this.date = date;
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
}
