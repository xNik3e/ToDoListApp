package com.example.todoplaceholder.models;

import android.graphics.Bitmap;

import com.example.todoplaceholder.utils.utils.PhotoHelper;

import java.util.List;

public class PhotoModels {
    private Bitmap mBitmap;
    private String filename;

    public PhotoModels(Bitmap bitmaps, String filename) {
        this.mBitmap = bitmaps;
        this.filename = filename;
    }

    public PhotoModels(String filename) {
        this.filename = filename;
        this.mBitmap = PhotoHelper.loadImageFromStorage(filename);
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
