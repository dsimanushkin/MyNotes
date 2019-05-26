package com.devlab74.mynotes.models;

import android.os.Parcel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    public Category(String title) {
        this.title = title;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        title = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Category{" +
                "title='" + title + '\'' +
                '}';
    }

    public static Category[] fillWithData() {
        return new Category[] {
                new Category("All notes"),
                new Category("Uncategorized")
        };
    }
}
