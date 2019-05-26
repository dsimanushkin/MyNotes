package com.devlab74.mynotes.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.devlab74.mynotes.NoteDatabase;
import com.devlab74.mynotes.daos.CategoryDao;
import com.devlab74.mynotes.models.Category;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;
    private Executor executor;

    public CategoryRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        categoryDao = database.categoryDao();
        allCategories = categoryDao.getAllCategories();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Category category) {
        executor.execute(() -> categoryDao.insert(category));
    }

    public void update(Category category) {
        executor.execute(() -> categoryDao.update(category));
    }

    public void delete(Category category) {
        executor.execute(() -> categoryDao.delete(category));
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}
