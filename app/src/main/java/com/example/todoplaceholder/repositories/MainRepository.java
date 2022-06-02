package com.example.todoplaceholder.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.utils.databaseUtils.MyRoomDatabase;
import com.example.todoplaceholder.utils.databaseUtils.CategoryDao;

import java.util.List;

public class MainRepository {

    private CategoryDao mCategoryDao;
    private LiveData<List<CategoryModel>> mCategoryList;

    public MainRepository(Application application){
        MyRoomDatabase db = MyRoomDatabase.getInstance(application);
        mCategoryDao = db.categoryDao();
    }

    //Queries handler
    public LiveData<List<CategoryModel>> getAllCategories(){
        return mCategoryDao.getCategories();
    }

    public LiveData<List<CategoryModel>> getCategoriesByName(String name){
        return mCategoryDao.getCategoriesByName(name);
    }

    public void insert(CategoryModel model){
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insert(model);
        });
    }

    public void deleteAll(){
        mCategoryDao.deleteAll();
    }

    public void insertAll(List<CategoryModel> models){
        mCategoryDao.insertAll(models);
    }
}
