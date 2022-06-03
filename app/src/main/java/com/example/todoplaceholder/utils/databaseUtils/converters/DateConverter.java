package com.example.todoplaceholder.utils.databaseUtils.converters;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.example.todoplaceholder.models.CategoryModel;
import com.google.gson.Gson;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

}
