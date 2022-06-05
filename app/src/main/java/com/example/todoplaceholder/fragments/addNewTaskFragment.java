package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class addNewTaskFragment extends BottomSheetDialogFragment {

    private Context context;
    private MainViewModel mainViewModel;
    private List<CategoryModel> categoryModels;
    private RecyclerView categoryRV;
    private TextInputLayout titleContainer, descriptionContainer, dateContainer, notificationContainer;
    private TextInputEditText title, description, date, notification;
    private CheckBox checkBox;
    private TextView addButton;

    private CategoryAdapter categoryAdapter;

    private int appColor;

    public addNewTaskFragment(MainViewModel MVM, List<CategoryModel> cModelList) {
        this.mainViewModel = MVM;
        this.categoryModels = cModelList;
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_new_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryRV = view.findViewById(R.id.recycler_view_category);
        titleContainer = view.findViewById(R.id.title_container);
        title = view.findViewById(R.id.mTitle);
        descriptionContainer = view.findViewById(R.id.description_container);
        description = view.findViewById(R.id.description);
        dateContainer = view.findViewById(R.id.date_container);
        date = view.findViewById(R.id.date);
        notificationContainer = view.findViewById(R.id.notification_container);
        notification = view.findViewById(R.id.notification);
        checkBox = view.findViewById(R.id.checkAlarm);
        addButton = view.findViewById(R.id.addButton);


        mainViewModel.getBaseColor().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                appColor = integer;
                setUIColors();
            }
        });

        categoryAdapter = new CategoryAdapter(context, categoryModels);
        categoryRV.setAdapter(categoryAdapter);
    }

    public void chooseDate(View view) {
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        for(CategoryModel m : categoryModels){
            m.setActive(false);
        }
        mainViewModel.insertAllCategories(categoryModels);
    }

    private void setUIColors() {

        titleContainer.setBoxStrokeColor(appColor);
        titleContainer.setBoxStrokeColorStateList(new ColorStateList(Globals.boxStates(), Globals.boxColors(appColor)));
        titleContainer.setDefaultHintTextColor(new ColorStateList(Globals.hintStates(), Globals.hintColors(appColor)));

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