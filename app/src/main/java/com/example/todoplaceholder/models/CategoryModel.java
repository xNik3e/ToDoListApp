package com.example.todoplaceholder.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.utils.view_services.App;

@Entity(tableName = "category_table")
public class CategoryModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String categoryName;

    private boolean isActive;

    private int colorId;
    @Ignore
    private int baseColor;
    @Ignore
    private int shadowColor;



    public CategoryModel(@NonNull String categoryName, boolean isActive, int colorId) {
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.colorId = colorId;
        createColorResources();
    }

    @Ignore
    public CategoryModel(String categoryName, int colorId) {
        this.categoryName = categoryName;
        this.colorId = colorId;
        isActive = false;
        createColorResources();
    }
    @Ignore
    public CategoryModel(String categoryName){
        this.categoryName = categoryName;
        this.colorId = 0;
        isActive = false;
        createColorResources();
    }

    private void createColorResources(){
       switch (colorId){
           case 1:
                this.baseColor = App.getContext().getResources().getColor(R.color.category1Main);
                this.shadowColor = App.getContext().getResources().getColor(R.color.category1Shadow);
                break;
           case 2:
               this.baseColor = App.getContext().getResources().getColor(R.color.category2Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category2Shadow);
               break;
           case 3:
               this.baseColor = App.getContext().getResources().getColor(R.color.category3Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category3Shadow);
               break;
           case 4:
               this.baseColor = App.getContext().getResources().getColor(R.color.category4Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category4Shadow);
               break;
           case 5:
               this.baseColor = App.getContext().getResources().getColor(R.color.category5Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category5Shadow);
               break;
           case 7:
               this.baseColor = App.getContext().getResources().getColor(R.color.category7Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category7Shadow);
               break;
           case 6:
           default:
               this.baseColor = App.getContext().getResources().getColor(R.color.category6Main);
               this.shadowColor = App.getContext().getResources().getColor(R.color.category6Shadow);
               break;
       }


    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getBaseColor() {
        return baseColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
