package com.devlab74.mynotes.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devlab74.mynotes.models.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category data);

    @Insert
    void insertAll(Category... categories);

    @Delete
    void delete(Category data);

    @Update
    void update(Category data);

    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAllCategories();

}
