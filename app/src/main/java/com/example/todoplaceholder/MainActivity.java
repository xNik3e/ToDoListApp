package com.example.todoplaceholder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.todoplaceholder.adapters.ColorAdapter;
import com.example.todoplaceholder.fragments.CategoriesFragment;
import com.example.todoplaceholder.fragments.SearchFragment;
import com.example.todoplaceholder.fragments.addNewCategoryFragment;
import com.example.todoplaceholder.fragments.addNewTaskFragment;
import com.example.todoplaceholder.fragments.mySettingsFragment;
import com.example.todoplaceholder.fragments.todoFragment;
import com.example.todoplaceholder.interfaces.BottomShelfInterface;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.ColorModel;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomShelfInterface, ColorAdapter.ChangeSelectionInterface {

    private MainViewModel mViewModel;

    public static final int NEW_CATEGORY_ACTIVITY_REQUEST_CODE = 1;
    private int appColor;
    private BottomNavigationView bottomNavigationView;
    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private FloatingActionButton fab;
    private int openedTab = 1;
    private todoFragment TODOFRAGMENT;
    private SearchFragment SEARCHFRAGMENT;
    private CategoriesFragment CATEGORIESFRAGMENT;
    private mySettingsFragment MYSETTINGSFRAGMENT;

    private addNewTaskFragment newTaskFragment;
    private addNewCategoryFragment newCategoryFragment;

    private List<ColorModel> colorModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI setup

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        fab = findViewById(R.id.fab);
        NavController navController = Navigation.findNavController(this, R.id.container);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        TODOFRAGMENT = new todoFragment();
        SEARCHFRAGMENT = new SearchFragment();
        CATEGORIESFRAGMENT = new CategoriesFragment();
        MYSETTINGSFRAGMENT = new mySettingsFragment();
        newTaskFragment = new addNewTaskFragment();
        newCategoryFragment = new addNewCategoryFragment();

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);



        mViewModel.getBaseColor().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todoFragment3:
                        setFragment(TODOFRAGMENT);
                        fab.setVisibility(View.VISIBLE);
                        openedTab = 1;
                        return true;
                    case R.id.searchFragment3:
                        setFragment(SEARCHFRAGMENT);
                        fab.setVisibility(View.GONE);
                        openedTab = 2;
                        return true;
                    case R.id.categoriesFragment2:
                        setFragment(CATEGORIESFRAGMENT);
                        fab.setVisibility(View.VISIBLE);
                        openedTab = 3;
                        return true;
                    case R.id.mySettingsFragment2:
                        setFragment(MYSETTINGSFRAGMENT);
                        fab.setVisibility(View.GONE);
                        openedTab = 4;
                        return true;
                }

                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (openedTab) {
                    case 1:
                        newTaskFragment.show(getSupportFragmentManager(), "TAG");
                        break;
                    case 3:
                        newCategoryFragment.show(getSupportFragmentManager(), "ADD");

                }
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    private void setUIColors() {

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // enabled
                new int[]{-android.R.attr.state_checked}};

        int[] colors = new int[]{
                appColor,
                getResources().getColor(R.color.white)};


        fab.setBackgroundTintList(ColorStateList.valueOf(appColor));
        bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
        bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CATEGORY_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CategoryModel model = new CategoryModel(data.getStringExtra("CATEGORY_NAME"), data.getIntExtra("COLOR_ID", 0));
            //mViewModel.addCategoryModel(model);
        } else {
            //do nothing
        }
    }

    @Override
    public void invertSelection(int position) {
        colorModels = mViewModel.getColorModelList().getValue();
        for(ColorModel model : colorModels){
            model.setActive(false);
        }
        colorModels.get(position).setActive(true);
        mViewModel.setColorList(colorModels);
    }
}