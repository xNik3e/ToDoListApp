package com.example.todoplaceholder;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback;
import com.example.todoplaceholder.adapters.CategoryAdapter;
import com.example.todoplaceholder.adapters.PhotosAdapter;
import com.example.todoplaceholder.interfaces.CategoryAdapterNotifier;
import com.example.todoplaceholder.interfaces.PhotoDeletionInterface;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.PhotoModels;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.Globals;
import com.example.todoplaceholder.utils.utils.DateTimePickerHandler;
import com.example.todoplaceholder.utils.utils.NotificationSchedule;
import com.example.todoplaceholder.utils.utils.PhotoHelper;
import com.example.todoplaceholder.viewmodels.MainViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private CategoryAdapterNotifier adapterNotifier;
    private CategoryAdapter categoryAdapter;
    private PhotosAdapter photosAdapter;
    private boolean isFirstTime = true;

    private ChoosePhotoHelper choosePhotoHelper;

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    private List<PhotoModels> photoModelsList = new ArrayList<>();
    private List<PhotoModels> allPhotoModels = new ArrayList<>();
    private int appColor;
    private DateTimePickerHandler endDateDateTimePickerHandler, notificationDateDateTimePickerHandler;
    private PhotoDeletionInterface photoDeletionInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        createNotificationChannel();

        Bundle bundle = getIntent().getExtras();
        int modelID = bundle.getInt("MODELID");
        //model = (TaskModel) bundle.getSerializable("MODEL");
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

        model = mainViewModel.getTaskByID(modelID);

        adapterNotifier = new CategoryAdapterNotifier() {
            @Override
            public void notifyAboutChange(int position, boolean isActive) {
                if (isActive) {
                    addButton.setBackgroundTintList(ColorStateList.valueOf(categoryModelList.get(position).getBaseColor()));
                } else {
                    if (model.getModel() != null)
                        addButton.setBackgroundTintList(ColorStateList.valueOf(model.getModel().getBaseColor()));
                    else
                        addButton.setBackgroundTintList(ColorStateList.valueOf(mainViewModel.getBaseColorNOW()));
                }
            }
        };

        photoDeletionInterface = new PhotoDeletionInterface() {
            @Override
            public void deletePhoto(PhotoModels model, int position) {
                photoModelsList.remove(model);
                PhotoHelper.deleteImageFromStorage(model.getFilename());
                photosAdapter.notifyItemRemoved(position);
            }
        };

        mainViewModel.getBaseColor().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (model.getModel() == null)
                    appColor = integer;
                else
                    appColor = model.getModel().getBaseColor();
                setUIColors();
            }
        });

        endDateDateTimePickerHandler = new DateTimePickerHandler(getSupportFragmentManager(), mainViewModel.getBaseColorNOW(), model.getEndDate().getTime());
        if (model.getNotificationTime() != null)
            notificationDateDateTimePickerHandler = new DateTimePickerHandler(getSupportFragmentManager(), mainViewModel.getBaseColorNOW(), model.getNotificationTime().getTime());
        else
            notificationDateDateTimePickerHandler = new DateTimePickerHandler(getSupportFragmentManager(), mainViewModel.getBaseColorNOW());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && dateContainer.getEditText().getText().length() == 0) {
                    Toast.makeText(EditActivity.this, "Cannot set alarm when end date is empty!", Toast.LENGTH_SHORT).show();
                    checkBox.setChecked(false);
                } else if (isChecked) {
                    notificationDateDateTimePickerHandler.setAppColor(model.getModel() == null ? appColor : model.getModel().getBaseColor());
                    notificationContainer.setVisibility(View.VISIBLE);

                    if (isFirstTime) {
                        notificationContainer.getEditText().setText(notificationDateDateTimePickerHandler.getDateAsString());
                    } else {
                        notificationDateDateTimePickerHandler.resetValues();
                        notificationContainer.getEditText().setText("");
                    }
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
                if (b) {
                    if (!checkBox.isChecked()) {
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
                if (b) {
                    if (checkBox.isChecked()) {
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


        endDateDateTimePickerHandler.getDateString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dateContainer.getEditText().setText(s);
            }
        });

        notificationDateDateTimePickerHandler.getDateString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                notificationContainer.getEditText().setText(s);
            }
        });


        categoryAdapter = new CategoryAdapter(this, categoryModelList, adapterNotifier);
        categoryRV.setAdapter(categoryAdapter);

        photosAdapter = new PhotosAdapter(this, photoModelsList, photoDeletionInterface);
        photosRV.setAdapter(photosAdapter);

        toolbarIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mainViewModel.getCategoryModels().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> modelList) {
                categoryModelList.clear();
                categoryModelList.addAll(modelList);
                categoryAdapter.notifyDataSetChanged();
                setAllData();
            }
        });

        attachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoHelper = ChoosePhotoHelper.with(EditActivity.this)
                        .asBitmap()
                        .build(new ChoosePhotoCallback<Bitmap>() {
                            @Override
                            public void onChoose(@Nullable Bitmap bitmap) {
                                String fileName = PhotoHelper.saveToInternalStorage(bitmap, null);
                                Bitmap tempBitmap = PhotoHelper.loadImageFromStorage(fileName);
                                model.getAttachedFileNames().add(fileName);
                                PhotoModels photoModel = new PhotoModels(tempBitmap, fileName);
                                photoModelsList.add(photoModel);
                                allPhotoModels.add(photoModel);
                                photosAdapter.notifyDataSetChanged();
                            }
                        });
                choosePhotoHelper.showChooser();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameContainer.getEditText().getText().length() == 0) {
                    Toast.makeText(EditActivity.this, "Title should not be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    if (descriptionContainer.getEditText().getText().length() > 300) {
                        Toast.makeText(EditActivity.this, "Description should not exceed 300 characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dateContainer.getEditText().getText().length() == 0) {
                            Toast.makeText(EditActivity.this, "Choose task end date!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (checkBox.isChecked() && notificationContainer.getEditText().getText().length() == 0) {
                                Toast.makeText(EditActivity.this, "Choose notification time!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (compareDates(endDateDateTimePickerHandler.getDateFinal(), notificationDateDateTimePickerHandler.getDateFinal())) {
                                    Toast.makeText(EditActivity.this, "Notification date should not exceed task end date!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //
                                    model.setTaskName(nameContainer.getEditText().getText().toString());
                                    model.setDescription(descriptionContainer.getEditText().getText().toString());
                                    List<CategoryModel> tempModels = new ArrayList<>();
                                    tempModels = categoryModelList.stream()
                                            .filter(CategoryModel::isActive)
                                            .collect(Collectors.toList());

                                    if (tempModels.size() != 1)
                                        model.setModel(null);
                                    else
                                        model.setModel(tempModels.get(0));

                                    model.setEndDate(new Date(endDateDateTimePickerHandler.getDateFinal()));

                                    Intent notifIntent = new Intent(EditActivity.this, NotificationSchedule.class);
                                    notifIntent.putExtra("TITLE", "Reminder for " + model.getTaskName());
                                    notifIntent.putExtra("DESCRIPTION", model.getDescription());
                                    notifIntent.putExtra("NOTIFICATION_ID", model.getNotificationUniqueID());

                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                    if (checkBox.isChecked()) {
                                        model.setNotificationTime(new Date(notificationDateDateTimePickerHandler.getDateFinal()));
                                        //NOTIFICATION CREATION
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(EditActivity.this, model.getNotificationUniqueID(),
                                                notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                                notificationDateDateTimePickerHandler.getDateFinal(),
                                                pendingIntent);

                                    } else{
                                        model.setNotificationTime(null);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(EditActivity.this, model.getNotificationUniqueID(),
                                                notifIntent, PendingIntent.FLAG_NO_CREATE);

                                        if(pendingIntent != null){
                                            alarmManager.cancel(pendingIntent);
                                        }
                                    }



                                    model.setAttachedFileBitmaps(new ArrayList<>());
                                    model.setAttachedFileNames(new ArrayList<>());
                                    if (!photoModelsList.isEmpty()) {
                                        IntStream.range(0, photoModelsList.size())
                                                .forEach(mIndex -> {
                                                    model.getAttachedFileBitmaps().add(photoModelsList.get(mIndex).getmBitmap());
                                                    model.getAttachedFileNames().add(photoModelsList.get(mIndex).getFilename());
                                                });

                                        List<String> usedPhotoNames = photoModelsList.stream().map(PhotoModels::getFilename).collect(Collectors.toList());
                                        List<String> allPhotoNames = allPhotoModels.stream().map(PhotoModels::getFilename).collect(Collectors.toList());

                                        allPhotoNames.stream()
                                                .filter(tempPhotoModel -> usedPhotoNames.stream().noneMatch(temp -> temp.equals(tempPhotoModel)))
                                                .forEach(PhotoHelper::deleteImageFromStorage);

                                    }
                                    mainViewModel.updateTask(model);
                                    Toast.makeText(EditActivity.this, "Changes applied", Toast.LENGTH_SHORT).show();
                                    //Intent
                                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                    if (!comesFrom.equals("FRAGMENT"))
                                        intent.putExtra("TO", "SEARCH");
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    //TODO METHODS

    private void setAllData() {
        List<Integer> index = new ArrayList<>();
        IntStream.range(0, categoryModelList.size()).filter(m -> {
            if (model.getModel() == null)
                return false;
            else {
                return model.getModel().getId() == categoryModelList.get(m).getId();
            }
        }).forEach(index::add);

        categoryModelList.forEach(m -> m.setActive(false));
        if (!index.isEmpty()) {
            categoryModelList.get(index.get(0)).setActive(true);
            addButton.setBackgroundTintList(ColorStateList.valueOf(categoryModelList.get(index.get(0)).getBaseColor()));
            categoryAdapter.notifyItemChanged(index.get(0));
        } else
            addButton.setBackgroundTintList(ColorStateList.valueOf(mainViewModel.getBaseColorNOW()));


        nameContainer.getEditText().setText(model.getTaskName());
        descriptionContainer.getEditText().setText(model.getDescription());

        endDateDateTimePickerHandler.setAppColor(model.getModel() == null ? appColor : model.getModel().getBaseColor());
        endDateDateTimePickerHandler.createFinalString(null);
        if (model.getNotificationTime() != null) {
            notificationDateDateTimePickerHandler.setAppColor(model.getModel() == null ? appColor : model.getModel().getBaseColor());
            notificationDateDateTimePickerHandler.createFinalString("EEE, dd/MM HH:mm");
            checkBox.setChecked(true);
        }

        photoModelsList.clear();
        if (!model.getAttachedFileNames().isEmpty()) {
            IntStream.range(0, model.getAttachedFileNames().size())
                    .forEach(mIndex -> {
                        photoModelsList.add(new PhotoModels(
                                model.getAttachedFileBitmaps().get(mIndex),
                                model.getAttachedFileNames().get(mIndex)
                        ));
                    });
        }
        photosAdapter.notifyDataSetChanged();

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


    private boolean compareDates(long endDate, long notificationDate) {
        return endDate < notificationDate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Reminder Channel";
            String description = "Channel for TODO reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("myTODOAPP", name, importance);
            channel.setDescription(description);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(EditActivity.this);
            notificationManagerCompat.createNotificationChannel(channel);
        }
    }

}