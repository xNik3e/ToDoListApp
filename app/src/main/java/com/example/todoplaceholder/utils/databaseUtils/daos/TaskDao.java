package com.example.todoplaceholder.utils.databaseUtils.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoplaceholder.models.TaskModel;

import java.util.List;


@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(TaskModel task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskModel> tasks);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table WHERE active = 1")
    LiveData<List<TaskModel>> getActiveTasks();

    @Query("SELECT * FROM task_table WHERE active = 0")
    LiveData<List<TaskModel>> getFinishedTasks();

    @Update
    void updateTask(TaskModel model);


}
