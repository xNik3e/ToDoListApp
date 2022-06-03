package com.example.todoplaceholder.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.repositories.ColorRepository;
import com.example.todoplaceholder.repositories.MainRepository;

import java.util.List;



public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> baseColor = new MutableLiveData<Integer>();
    private MutableLiveData<List<CategoryModel>> categoryModels = new MutableLiveData<List<CategoryModel>>();
    private MutableLiveData<List<TaskModel>> taskModels = new MutableLiveData<List<TaskModel>>();

    private MainRepository mRepository;
    private ColorRepository colorRepository;

    public MainViewModel(Application application){
        super(application);
        mRepository = new MainRepository(application);
        colorRepository = ColorRepository.getInstance();
        categoryModels = (MutableLiveData<List<CategoryModel>>) mRepository.getAllCategories();
        baseColor = (MutableLiveData<Integer>) colorRepository.getColorData(application);
    }

    //COLOR HANDLER

    public void setBaseColor(int color){
        baseColor.setValue(color);
    }
    public LiveData<Integer> getBaseColor(){
        return baseColor;
    }

    //CATEGORY HANDLERS

    public void setCategoryModels(List<CategoryModel> list){
        mRepository.insertAllCategories(list);
    }
    public void addCategoryModel(CategoryModel model){
        mRepository.insert(model);
    }

    public LiveData<List<CategoryModel>> getCategoriesByName(String name){
        categoryModels = (MutableLiveData<List<CategoryModel>>) mRepository.getCategoriesByName(name);
        return categoryModels;
    }

    public LiveData<List<CategoryModel>> getCategoryModels(){
        return categoryModels;
    }

    public void removeAllCategories(){
        mRepository.deleteAllCategories();
    }

    public void updateCategory(CategoryModel model){
        mRepository.updateCategory(model);
    }

    //TASK HANDLERS
    public void setTaskModels(List<TaskModel> list){
        mRepository.insertAllTasks(list);
    }

    public void insertTask(TaskModel task){mRepository.insertTask(task);}

    public void deleteAllTasks(){mRepository.deleteAllTasks();}

    public LiveData<List<TaskModel>> getActiveTasks(){return mRepository.getActiveTasks();}
    public LiveData<List<TaskModel>> getFinishedTasks(){return mRepository.getFinishedTasks();}
    public void updateTask(TaskModel task){mRepository.updateTask(task);}
}
