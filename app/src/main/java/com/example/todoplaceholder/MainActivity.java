package com.example.todoplaceholder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    public static final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 1;
    private int appColor;
    private BottomNavigationView bottomNavigationView;
    private List<CategoryModel> categoryModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI setup

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);



        mViewModel.getBaseColor().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });
    }

    private void setUIColors() {

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // enabled
                new int[]{-android.R.attr.state_checked}};

        int[] colors = new int[]{
                appColor,
                getResources().getColor(R.color.white)};

        bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
        bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CategoryModel model = new CategoryModel(data.getStringExtra("CATEGORY_NAME"), data.getIntExtra("COLOR_ID", 0));
            mViewModel.addCategoryModel(model);
        } else {
            //do nothing
        }
    }
}