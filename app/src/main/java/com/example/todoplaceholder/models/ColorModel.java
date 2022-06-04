package com.example.todoplaceholder.models;

import android.content.res.Resources;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.utils.view_services.App;

import java.util.ArrayList;
import java.util.List;

public class ColorModel {

    private boolean isActive;
    private int baseColor;

    public ColorModel(boolean isActive, int baseColor) {
        this.isActive = isActive;
        this.baseColor = baseColor;

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }


    public static List<ColorModel> populateColorList() {
        List<ColorModel> temp = new ArrayList<>();

        Resources res = App.getContext().getResources();

        temp.add(new ColorModel(false, res.getColor(R.color.category1Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category2Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category3Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category4Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category5Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category6Main)));
        temp.add(new ColorModel(false, res.getColor(R.color.category7Main)));

        return temp;
    }

}
