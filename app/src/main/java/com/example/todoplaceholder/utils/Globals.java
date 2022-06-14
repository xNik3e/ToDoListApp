package com.example.todoplaceholder.utils;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import java.lang.reflect.Field;

public class Globals {
    public static int[][] boxStates() {
        return new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{-android.R.attr.state_focused}, // unchecked
                new int[]{android.R.attr.state_focused}, // unchecked
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_active}// pressed
        };
    }

    public static int[] boxColors(int appColor) {
        return new int[]{
                appColor,
                appColor,
                appColor,
                appColor,
                appColor,
                appColor,
                appColor,
                appColor
        };
    }

    public static int[][] hintStates() {
        return new int[][]{
                new int[]{-android.R.attr.state_focused}, // unchecked
                new int[]{android.R.attr.state_focused} // unchecked
        };
    }
    public static int[] hintColors(int appColor) {
        return new int[]{
                ColorUtils.blendARGB(appColor, Color.BLACK, 0.6f),
                appColor
        };
    }



}
