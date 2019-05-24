package com.devlab74.mynotes;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab74.mynotes.adapters.NoteRecyclerAdapter;
import com.devlab74.mynotes.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends BaseActivity {

    private List<Note> notesList;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NoteRecyclerAdapter noteRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        floatingActionButton = findViewById(R.id.fab);

        initRecyclerView();

        // For testing only
        if (notesList == null) {
            notesList = new ArrayList<>();
            notesList.add(new Note());
            noteRecyclerAdapter.setNotes(notesList);
        }
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        noteRecyclerAdapter = new NoteRecyclerAdapter();
        recyclerView.setAdapter(noteRecyclerAdapter);
    }
}
