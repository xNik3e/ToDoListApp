package com.example.todoplaceholder.utils.databaseUtils;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.example.todoplaceholder.models.CategoryModel;
import com.google.gson.Gson;

import java.util.Date;

@ProvidedTypeConverter
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public synchronized String CategoryToString(CategoryModel model){
        Gson gson = new Gson();
        String json = gson.toJson(model);
        return json;
    }

    @TypeConverter
    public synchronized CategoryModel StringToCategory(String json){
        Gson gson = new Gson();
        CategoryModel model = gson.fromJson(json, CategoryModel.class);
        return model;
    }

}
