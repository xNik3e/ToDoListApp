package com.example.todoplaceholder.utils.databaseUtils.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoplaceholder.models.CategoryModel;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(CategoryModel model);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryModel> models);

    @Query("DELETE FROM category_table")
    void deleteAll();

    @Query("SELECT * FROM category_table ORDER BY id")
    LiveData<List<CategoryModel>> getCategories();

    @Query("SELECT * FROM CATEGORY_TABLE WHERE categoryName = :catName")
    LiveData<List<CategoryModel>> getCategoriesByName(String catName);

    @Update
    void updateCategory(CategoryModel model);
}
