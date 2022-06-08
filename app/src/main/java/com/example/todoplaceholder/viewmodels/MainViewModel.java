package com.example.todoplaceholder.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoplaceholder.R;
import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.ColorModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.repositories.ColorRepository;
import com.example.todoplaceholder.repositories.MainRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> baseColor = new MutableLiveData<Integer>();
    private final LiveData<List<CategoryModel>> categoryModels;
    private final LiveData<List<TaskModel>> taskModels;
    private MutableLiveData<List<ColorModel>> colorList = new MutableLiveData<>();
    private final int colorData;


    private MainRepository mRepository;
    private ColorRepository colorRepository;

    public MainViewModel(Application application) {
        super(application);
        mRepository = new MainRepository(application);
        colorRepository = ColorRepository.getInstance();
        baseColor.setValue(colorRepository.getColorData(application));
        categoryModels = mRepository.getAllCategories();
        taskModels = mRepository.getAllTasks();
        colorList.setValue(ColorModel.populateColorList());
        colorData = colorRepository.getColorData(application);
    }

    //TODO COLOR HANDLER

    public void setBaseColor(int color) {
        baseColor.setValue(color);
    }

    public int getBaseColorNOW() {
        return colorData;
    }

    public LiveData<Integer> getBaseColor() {
        return baseColor;
    }

    public void setColorList(List<ColorModel> models) {
        colorList.setValue(models);
    }

    public LiveData<List<ColorModel>> getColorModelList() {
        return colorList;
    }

    //TODO CATEGORY HANDLERS

    public LiveData<List<CategoryModel>> getCategoryModels() {
        return categoryModels;
    }

    public void insertAllCategories(List<CategoryModel> models) {
        mRepository.insertAllCategories(models);
    }

    public void insertCategory(CategoryModel model) {
        mRepository.insertCategory(model);
    }

    public CategoryModel getCategoryByName(String name) {
        return mRepository.getCategoryByName(name);
    }

    public void updateCategory(CategoryModel model) {
        mRepository.updateCategory(model);
    }

    public void deleteAllCategories() {
        mRepository.deleteAllCategories();
    }

    public void deleteCategory(String name) {
        mRepository.deleteCategory(name);
    }

    //TODO TASK HANDLERS

    public LiveData<List<TaskModel>> getTaskModels() {
        return taskModels;
    }

    public void insertTask(TaskModel task) {
        mRepository.insertTask(task);
    }

    public void deleteTask(String name) {
        mRepository.deleteTask(name);
    }

    public void insertAllTasks(List<TaskModel> tasks) {
        mRepository.insertAllTasks(tasks);
    }

}
