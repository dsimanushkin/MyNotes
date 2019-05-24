package com.devlab74.mynotes.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.devlab74.mynotes.models.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note data);

    @Delete
    void delete(Note data);

    @Update
    void update(Note data);

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllNotes();
}
