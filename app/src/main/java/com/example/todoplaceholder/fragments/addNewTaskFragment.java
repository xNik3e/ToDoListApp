package com.example.todoplaceholder.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.calendarview.PrimeCalendarView;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
import com.example.todoplaceholder.R;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.interfaces.CategoryAdapterNotifier;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.view_services.App;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.michaldrabik.classicmaterialtimepicker.CmtpTimeDialogFragment;
import com.michaldrabik.classicmaterialtimepicker.OnTime24PickedListener;
import com.michaldrabik.classicmaterialtimepicker.model.CmtpTime24;

import org.checkerframework.checker.units.qual.C;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private long time;

    private Date calendarDate;
    private int minuteValue;
    private int hourValue;
    private long dateFinal = 0;

    private int appColor = 0;

    private PrimeDatePicker datePicker = null;

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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    notificationContainer.setVisibility(View.VISIBLE);
                } else {
                    notificationContainer.setVisibility(View.GONE);
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingleDayPickCallback callback = new SingleDayPickCallback() {
                    @Override
                    public void onSingleDayPicked(PrimeCalendar singleDay) {
                        calendarDate = null;
                        long dateValue = singleDay.getTimeInMillis();
                        calendarDate = new Date(dateValue);

                        CmtpTimeDialogFragment timePicker = CmtpTimeDialogFragment.newInstance();

                        if (dateFinal != 0) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date(dateFinal));
                            timePicker.setInitialTime24(c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
                        } else {
                            timePicker.setInitialTime24(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
                        }

                        timePicker.show(getChildFragmentManager(), "TIME_PICKER");

                        timePicker.setOnTime24PickedListener(new OnTime24PickedListener() {
                            @Override
                            public void onTimePicked(@NonNull CmtpTime24 cmtpTime24) {
                                hourValue = cmtpTime24.getHour();
                                minuteValue = cmtpTime24.getMinute();
                                setDateContainer();
                            }
                        });

                    }
                };

                PrimeCalendar today = new CivilCalendar();
                today.setTimeInMillis(System.currentTimeMillis());

                if (dateFinal != 0) {
                    PrimeCalendar pickedDay = new CivilCalendar();
                    pickedDay.setTimeInMillis(dateFinal);

                    datePicker = PrimeDatePicker.Companion.dialogWith(today)
                            .pickSingleDay(callback)
                            .initiallyPickedSingleDay(pickedDay)
                            .firstDayOfWeek(Calendar.MONDAY)
                            .minPossibleDate(today)
                            .applyTheme(getDarkTheme(appColor))
                            .build();
                } else {
                    datePicker = PrimeDatePicker.Companion.dialogWith(today)
                            .pickSingleDay(callback)
                            .firstDayOfWeek(Calendar.MONDAY)
                            .minPossibleDate(today)
                            .applyTheme(getDarkTheme(appColor))
                            .build();
                }


                datePicker.show(getChildFragmentManager(), "SOME_TAG");

                datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        datePicker = null;
                    }
                });
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
                                //
                            }
                        }
                    }
                }
            }
        });

        dateContainer.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFinal = 0;
                dateContainer.getEditText().setText("");
            }
        });


    }

    private void setDateContainer() {
        Calendar c = Calendar.getInstance();
        c.setTime(calendarDate);

        if (hourValue == -1) {
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
        } else {
            c.set(Calendar.HOUR, hourValue);
            c.set(Calendar.MINUTE, minuteValue);
        }
        this.dateFinal = c.getTimeInMillis();

        SimpleDateFormat smf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
        Toast.makeText(context, smf.format(dateFinal), Toast.LENGTH_SHORT).show();

        dateContainer.getEditText().setText(smf.format(dateFinal));
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        for (CategoryModel m : categoryModels) {
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
}