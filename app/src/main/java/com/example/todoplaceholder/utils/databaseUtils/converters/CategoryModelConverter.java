package com.example.todoplaceholder.utils.databaseUtils.converters;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.example.todoplaceholder.models.CategoryModel;
import com.google.gson.Gson;

public class CategoryModelConverter {

    @TypeConverter
    public static String CategoryToString(CategoryModel model){
        Gson gson = new Gson();
        String json = gson.toJson(model);
        return json;
    }

    @TypeConverter
    public static CategoryModel StringToCategory(String json){
        Gson gson = new Gson();
        CategoryModel model = gson.fromJson(json, CategoryModel.class);
        return model;
    }
}
