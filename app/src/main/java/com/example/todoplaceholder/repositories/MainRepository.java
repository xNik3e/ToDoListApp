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
        mCategoryList = mCategoryDao.getCategories();
        mTaskList = mTaskDao.getAllTasks();
    }

    //TODO CATEGORIES

    public LiveData<List<CategoryModel>> getAllCategories() {
        return mCategoryList;
    }

    public void insertCategory(CategoryModel model) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insert(model);
        });
    }

    public void insertAllCategories(List<CategoryModel> list) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insertAll(list);
        });
    }

    public CategoryModel getCategoryByName(String name) {
        return mCategoryDao.getCategoryByName(name);
    }

    public void updateCategory(CategoryModel model) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.updateCategory(model);
        });
    }

    public void deleteAllCategories() {
        mCategoryDao.deleteAll();
    }

    public void deleteCategory(String name) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.deleteCategory(name);
        });
    }

    //TODO TASKS

    public LiveData<List<TaskModel>> getAllTasks() {
        return mTaskList;
    }

    public void deleteAllTasks() {
        mTaskDao.deleteAll();
    }

    public void updateTask(TaskModel task) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.updateTask(task);
        });
    }

    public void insertTask(TaskModel task) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    public void insertAllTasks(List<TaskModel> tasks) {
        MyRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insertAll(tasks);
        });
    }

    public List<TaskModel> getAllActiveTasks() {
        return mTaskDao.getActiveTasks();
    }

    public List<TaskModel> getAllFinishedTasks() {
        return mTaskDao.getFinishedTasks();
    }

    public TaskModel selectTaskById(int taskID){
        return mTaskDao.getTaskById(taskID);
    }

}


























