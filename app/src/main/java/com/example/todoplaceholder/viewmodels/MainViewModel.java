package com.example.todoplaceholder.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.repositories.MainRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> baseColor = new MutableLiveData<Integer>();
    private MutableLiveData<List<CategoryModel>> categoryModels = new MutableLiveData<List<CategoryModel>>();

    private MainRepository mRepository;

    public MainViewModel(Application application){
        super(application);
        mRepository = new MainRepository(application);
        categoryModels = (MutableLiveData<List<CategoryModel>>) mRepository.getAllCategories();
    }


    public void setBaseColor(int color){
        baseColor.setValue(color);
    }

    public LiveData<Integer> getBaseColor(){
        return baseColor;
    }

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
        mRepository.deleteAll();
    }

    public void updateCategory(CategoryModel model){
        mRepository.updateCategory(model);
    }
}
