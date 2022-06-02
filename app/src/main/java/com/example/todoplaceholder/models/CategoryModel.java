package com.example.todoplaceholder.models;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todoplaceholder.R;
@Entity(tableName = "category_table")
public class CategoryModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @ColumnInfo(name = "colorKey")
    private int colorId;
    @ColumnInfo(name = "baseColor")
    private int baseColor;
    @ColumnInfo(name = "shadowColor")
    private int shadowColor;



    public CategoryModel(String categoryName, int colorId) {
        this.categoryName = categoryName;
        this.colorId = colorId;
        createColorResources();
    }
    @Ignore
    public CategoryModel(String categoryName){
        this.categoryName = categoryName;
        this.colorId = 0;
        createColorResources();
    }

    private void createColorResources(){
       switch (colorId){
           case 1:
                this.baseColor = Resources.getSystem().getColor(R.color.category1Main);
                this.shadowColor = Resources.getSystem().getColor(R.color.category1Shadow);
                break;
           case 2:
               this.baseColor = Resources.getSystem().getColor(R.color.category2Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category2Shadow);
               break;
           case 3:
               this.baseColor = Resources.getSystem().getColor(R.color.category3Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category3Shadow);
               break;
           case 4:
               this.baseColor = Resources.getSystem().getColor(R.color.category4Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category4Shadow);
               break;
           case 5:
               this.baseColor = Resources.getSystem().getColor(R.color.category5Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category5Shadow);
               break;
           case 7:
               this.baseColor = Resources.getSystem().getColor(R.color.category7Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category7Shadow);
               break;
           case 6:
           default:
               this.baseColor = Resources.getSystem().getColor(R.color.category6Main);
               this.shadowColor = Resources.getSystem().getColor(R.color.category6Shadow);
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
}
