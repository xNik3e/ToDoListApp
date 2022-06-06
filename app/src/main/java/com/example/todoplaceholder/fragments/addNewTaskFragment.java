package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.interfaces.CategoryAdapterNotifier;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.utils.DateTimePickerHandler;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
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
    private CategoryAdapterNotifier adapterNotifier;
    private CategoryAdapter categoryAdapter;
    private DateTimePickerHandler endDateDateTimePickerHandler, notificationDateDateTimePickerHandler;


    private int appColor = 0;

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

        categoryAdapter = new CategoryAdapter(context, categoryModels, adapterNotifier);
        categoryRV.setAdapter(categoryAdapter);

        adapterNotifier = new CategoryAdapterNotifier() {
            @Override
            public void notifyAboutChange(int position, boolean isActive) {
                if (isActive) {
                    addButton.setBackgroundTintList(ColorStateList.valueOf(categoryModels.get(position).getBaseColor()));
                } else {
                    if (appColor != 0) {
                        addButton.setBackgroundTintList(ColorStateList.valueOf(appColor));
                    } else {
                        addButton.setBackgroundTintList(ColorStateList.valueOf(mainViewModel.getBaseColor().getValue()));
                    }
                }
            }
        };

        endDateDateTimePickerHandler = new DateTimePickerHandler(getChildFragmentManager(), appColor);
        notificationDateDateTimePickerHandler = new DateTimePickerHandler(getChildFragmentManager(), appColor);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked && dateContainer.getEditText().getText().length() == 0){
                    Toast.makeText(context, "Cannot set alarm when end date is empty!", Toast.LENGTH_SHORT).show();
                    checkBox.setChecked(false);
                }
                else if(isChecked) {
                    notificationContainer.setVisibility(View.VISIBLE);
                    notificationDateDateTimePickerHandler.resetValues();
                    notificationContainer.getEditText().setText("");
                } else {
                    notificationContainer.setVisibility(View.GONE);
                    notificationDateDateTimePickerHandler.resetValues();
                    notificationContainer.getEditText().setText("");

                }
            }
        });



        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    if(!checkBox.isChecked()){
                        endDateDateTimePickerHandler.createDateTimePicker(null, -1);
                        checkBox.setChecked(false);
                    }
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDateDateTimePickerHandler.createDateTimePicker(null, -1);
            }
        });

        notification.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    if(checkBox.isChecked()){
                        notificationDateDateTimePickerHandler.createDateTimePicker("EEE, dd/MM HH:mm", endDateDateTimePickerHandler.getDateFinal());
                    }
                }

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDateDateTimePickerHandler.createDateTimePicker("EEE, dd/MM HH:mm", endDateDateTimePickerHandler.getDateFinal());
            }
        });


        endDateDateTimePickerHandler.getDateString().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dateContainer.getEditText().setText(s);
            }
        });

        notificationDateDateTimePickerHandler.getDateString().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                notificationContainer.getEditText().setText(s);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleContainer.getEditText().getText().length() == 0) {
                    Toast.makeText(context, "Title should not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    if (descriptionContainer.getEditText().getText().length() > 300) {
                        Toast.makeText(context, "Description should not exceed 300 characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dateContainer.getEditText().getText().length() == 0) {
                            Toast.makeText(context, "Choose task end date!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (checkBox.isChecked() && notificationContainer.getEditText().getText().length() == 0) {
                                Toast.makeText(context, "Choose notification time!", Toast.LENGTH_SHORT).show();
                            } else {
                                if(compareDates(endDateDateTimePickerHandler.getDateFinal(), notificationDateDateTimePickerHandler.getDateFinal())){
                                    Toast.makeText(context, "Notification date should not exceed task end date!!!", Toast.LENGTH_SHORT).show();
                                }else{
                                    //
                                }
                            }
                        }
                    }
                }
            }
        });

        dateContainer.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateContainer.getEditText().setText("");
                endDateDateTimePickerHandler.resetValues();
                checkBox.setChecked(false);
            }
        });

        notificationContainer.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationContainer.getEditText().setText("");
                notificationDateDateTimePickerHandler.resetValues();
            }
        });


    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        for (CategoryModel m : categoryModels) {
            m.setActive(false);
        }
        mainViewModel.insertAllCategories(categoryModels);
        dateContainer.getEditText().setText("");
        titleContainer.getEditText().setText("");
        descriptionContainer.getEditText().setText("");
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

    private DarkThemeFactory getDarkTheme(int appColor) {
        return new DarkThemeFactory() {
            @Override
            public int getDialogBackgroundColor() {
                return super.getColor(R.color.cardBackground);
            }

            @Override
            public int getCalendarViewBackgroundColor() {
                return super.getColor(R.color.cardBackground);
            }

            @Override
            public int getCalendarViewPickedDayBackgroundColor() {
                return appColor;
            }

            @Override
            public int getCalendarViewMonthLabelTextColor() {
                return appColor;
            }

            @Override
            public int getCalendarViewTodayLabelTextColor() {
                return super.getColor(R.color.defaultColor);
            }

            @Override
            public SparseIntArray getCalendarViewWeekLabelTextColors() {
                SparseIntArray tempArray = new SparseIntArray(7);
                int darkerColor = ColorUtils.blendARGB(appColor, Color.BLACK, 0.4f);

                tempArray.put(Calendar.SATURDAY, appColor);
                tempArray.put(Calendar.SUNDAY, appColor);
                tempArray.put(Calendar.MONDAY, darkerColor);
                tempArray.put(Calendar.TUESDAY, darkerColor);
                tempArray.put(Calendar.WEDNESDAY, darkerColor);
                tempArray.put(Calendar.THURSDAY, darkerColor);
                tempArray.put(Calendar.FRIDAY, darkerColor);
                return tempArray;
            }

            @Override
            public int getSelectionBarBackgroundColor() {
                return appColor;
            }

            @Override
            public int getGotoViewBackgroundColor() {
                return appColor;
            }

        };

    }

    private boolean compareDates(long endDate, long notificationDate){
        return endDate < notificationDate;
    }


}