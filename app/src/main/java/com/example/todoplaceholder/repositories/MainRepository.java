package com.example.todoplaceholder.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todoplaceholder.models.CategoryModel;
import com.example.todoplaceholder.models.TaskModel;
import com.example.todoplaceholder.utils.databaseUtils.MyRoomDatabase;
import com.example.todoplaceholder.utils.databaseUtils.daos.CategoryDao;
import com.example.todoplaceholder.utils.databaseUtils.daos.TaskDao;

import java.util.List;

public class MainRepository {

    private CategoryDao mCategoryDao;
    private TaskDao mTaskDao;

    private LiveData<List<CategoryModel>> mCategoryList;
    private LiveData<List<TaskModel>> mTaskList;

    public MainRepository(Application application) {
        MyRoomDatabase db = MyRoomDatabase.getInstance(application);
        mCategoryDao = db.categoryDao();
        mTaskDao = db.taskDao();
    }

    //TODO Queries handler
    //Category
    public LiveData<List<CategoryModel>> getAllCategories() {
        return mCategoryDao.getCategories();
    }

    public LiveData<List<CategoryModel>> getCategoriesByName(String name) {
        return mCategoryDao.getCategoriesByName(name);
    }

    public void insert(CategoryModel model) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insert(model);
        });
    }

    public void deleteAllCategories() {
        mCategoryDao.deleteAll();
    }

    public void insertAllCategories(List<CategoryModel> models) {
        mCategoryDao.insertAll(models);
    }

    public void updateCategory(CategoryModel model) {
        mCategoryDao.updateCategory(model);
    }

    //Task
    public void insertTask(TaskModel task) {
        mTaskDao.insert(task);
    }

    public void insertAllTasks(List<TaskModel> models) {
        mTaskDao.insertAll(models);
    }

    public void deleteAllTasks() {
        mTaskDao.deleteAll();
    }

    public LiveData<List<TaskModel>> getActiveTasks() {
        return mTaskDao.getActiveTasks();
    }

    public LiveData<List<TaskModel>> getFinishedTasks() {
        return mTaskDao.getFinishedTasks();
    }   

    public void updateTask(TaskModel task) {
        mTaskDao.updateTask(task);
    }

}
