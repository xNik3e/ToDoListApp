package com.example.todoplaceholder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private String comesFrom;
    private TaskModel model;
    private FrameLayout toolbarIconContainer;
    private TextInputLayout nameContainer, descriptionContainer, dateContainer, notificationContainer;
    private TextInputEditText name, description, date, notification;
    private MainViewModel mainViewModel;
    private RecyclerView categoryRV, photosRV;
    private TextView attachPhoto, addButton;
    private CheckBox checkBox;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private int appColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle bundle = getIntent().getExtras();
        model = (TaskModel) bundle.getSerializable("MODEL");
        comesFrom = bundle.getString("FROM");

        toolbarIconContainer = findViewById(R.id.toolbar_icon_container);
        nameContainer = findViewById(R.id.name_container);
        descriptionContainer = findViewById(R.id.description_container);
        dateContainer = findViewById(R.id.date_container);
        notificationContainer = findViewById(R.id.notification_container);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        notification = findViewById(R.id.notification);

        categoryRV = findViewById(R.id.recycler_view_category);
        photosRV = findViewById(R.id.recycler_view_photos);

        attachPhoto = findViewById(R.id.attach_photo);
        checkBox = findViewById(R.id.checkAlarm);
        addButton = findViewById(R.id.addButton);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getCategoryModels().observe(this, new androidx.lifecycle.Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> modelList) {
                categoryModelList.clear();
                categoryModelList.addAll(modelList);
            }
        });

        mainViewModel.getBaseColor().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });


    }

    public void chooseDate(View view) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        if (!comesFrom.equals("FRAGMENT"))
            intent.putExtra("TO", "SEARCH");

        startActivity(intent);
        finish();
    }

    private void setUIColors() {

        nameContainer.setBoxStrokeColor(appColor);
        nameContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        nameContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));

        descriptionContainer.setBoxStrokeColor(appColor);
        descriptionContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        descriptionContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));

        dateContainer.setBoxStrokeColor(appColor);
        dateContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        dateContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));
        dateContainer.setStartIconTintList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));


        notificationContainer.setBoxStrokeColor(appColor);
        notificationContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        notificationContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));
        notificationContainer.setStartIconTintList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));

        checkBox.setButtonTintList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        addButton.setBackgroundTintList(ColorStateList.valueOf(appColor));

    }
}