package com.example.todoplaceholder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private List<CategoryModel> categoryModelList;
    public static final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI setup

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavController navController = Navigation.findNavController(this, R.id.container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getCategoryModels().observe(this, categoryModels -> {
            //Update a cached copy of the categories
            this.categoryModelList.clear();
            this.categoryModelList.addAll(categoryModels);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CategoryModel model = new CategoryModel(data.getStringExtra("CATEGORY_NAME"), data.getIntExtra("COLOR_ID", 0));
            mViewModel.addCategoryModel(model);
        }else{
            //do nothing
        }
    }
}