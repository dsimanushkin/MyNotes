package com.devlab74.mynotes;

import android.os.Bundle;
import android.view.View;

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
        floatingActionButton.setOnClickListener(onClickListener);

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        noteRecyclerAdapter = new NoteRecyclerAdapter();
        recyclerView.setAdapter(noteRecyclerAdapter);
    }

    private View.OnClickListener onClickListener = view -> {
        if (notesList == null) {
            notesList = new ArrayList<>();
        }
        Note note = new Note();
        notesList.add(note);
        noteRecyclerAdapter.setNotes(notesList);
    };
}
