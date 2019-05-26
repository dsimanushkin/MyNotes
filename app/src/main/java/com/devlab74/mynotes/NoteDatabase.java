package com.devlab74.mynotes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devlab74.mynotes.daos.CategoryDao;
import com.devlab74.mynotes.daos.NoteDao;
import com.devlab74.mynotes.models.Category;
import com.devlab74.mynotes.models.Note;
import com.devlab74.mynotes.typeconverters.CategoryTypeConverter;
import com.devlab74.mynotes.typeconverters.DateTypeConverters;

import java.util.concurrent.Executors;

@Database(entities = {Note.class, Category.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverters.class, CategoryTypeConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase INSTANCE;

    public abstract NoteDao noteDao();
    public abstract CategoryDao categoryDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(context).categoryDao().insertAll(Category.fillWithData()));
                        }
                    }).build();
        }
        return INSTANCE;
    }
}
