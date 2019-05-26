package com.devlab74.mynotes.typeconverters;

import androidx.room.TypeConverter;

import com.devlab74.mynotes.models.Category;
import com.google.gson.Gson;

public class CategoryTypeConverter {

    @TypeConverter
    public static String toString(Category category) {
        return new Gson().toJson(category);
    }

    @TypeConverter
    public static Category toCategory(String value) {
        return new Gson().fromJson(value, Category.class);
    }

}
