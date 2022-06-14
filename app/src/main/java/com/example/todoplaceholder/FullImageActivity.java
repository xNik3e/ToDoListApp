package com.example.todoplaceholder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.todoplaceholder.utils.utils.PhotoHelper;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        String filename = getIntent().getStringExtra("FILENAME");
        Bitmap bitmap = PhotoHelper.loadImageFromStorage(filename);

        PhotoView photoView = findViewById(R.id.photoview);

        Glide.with(this)
                .load(bitmap)
                .placeholder(R.drawable.default_placeholder)
                .into(photoView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}