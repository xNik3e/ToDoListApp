package com.example.todoplaceholder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AboutMeActivity extends AppCompatActivity {

    private ImageView animationFS;
    private ImageView goBack;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        description = findViewById(R.id.textDescription);
        animationFS = findViewById(R.id.animation);
        Glide.with(this).load(R.drawable.flutter_jam).into(animationFS);

        description.setText("TODO app made by Norbert Nielaba\nIndex number: 228810");


    }

    public void returnToMain(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AboutMeActivity.this, MainActivity.class);
        intent.putExtra("TO", "SETTINGS");
        startActivity(intent);
        finish();
    }
}