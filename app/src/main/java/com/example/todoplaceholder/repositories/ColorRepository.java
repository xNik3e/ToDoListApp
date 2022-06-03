package com.example.todoplaceholder.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoplaceholder.R;

public class ColorRepository {

    private static final String PREF_NAME = "com.example.todoplaceholder_preferences";
    private static final String KEY = "colorPref";
    private static SharedPreferences preferences;
    private int colorData;
    private static ColorRepository INSTANCE;

    public static ColorRepository getInstance(){
        if(INSTANCE == null)
            INSTANCE = new ColorRepository();
        return INSTANCE;
    }

    public int getColorData(Context context){
        retrieveColorData(context);
        return colorData;
    }

    public void saveColorData(Context context, int colorData){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY, colorData);
        editor.apply();
    }

    private void retrieveColorData(Context context){
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int color = preferences.getInt(KEY, 0);
        colorData=color;
    }
}
