package com.devlab74.mynotes;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab74.mynotes.adapters.NoteRecyclerAdapter;
import com.devlab74.mynotes.models.Note;
import com.devlab74.mynotes.viewmodels.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

public class NotesActivity extends BaseActivity {

    private NoteViewModel noteViewModel;

    private static final int ADD_NOTE_REQUEST = 0;
    private static final int EDIT_NOTE_REQUEST = 1;

    private List<Note> notesList;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NoteRecyclerAdapter noteRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        setContentView(R.layout.activity_notes);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(onClickListener);

        initRecyclerView();
        subscribeObservers();
    }

    private void subscribeObservers() {
        noteViewModel.getAllNotes().observe(this, notes -> {
            notesList = notes;
            noteRecyclerAdapter.submitList(notesList);
            noteRecyclerAdapter.setNotes(notesList);
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        noteRecyclerAdapter = new NoteRecyclerAdapter();
        recyclerView.setAdapter(noteRecyclerAdapter);
        noteRecyclerAdapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            intent.putExtra(AddEditNoteActivity.EXTRA_DATE_CREATED, note.getDateCreated());
            intent.putExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH, note.getOptionalImagePath());
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteRecyclerAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Snackbar.make(findViewById(R.id.activity_content), R.string.note_deleted, Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private View.OnClickListener onClickListener = view -> {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String imagePath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);

            Note note = new Note(title, description, dateCreated, dateUpdated, imagePath);

            noteViewModel.insert(note);
            Snackbar.make(findViewById(R.id.activity_content), R.string.note_saved, Snackbar.LENGTH_LONG).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String imagePath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);

            Note note = new Note(title, description, dateCreated, dateUpdated, imagePath);

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Snackbar.make(findViewById(R.id.activity_content), R.string.note_not_updated, Snackbar.LENGTH_LONG).show();
                return;
            }

            note.setId(id);
            noteViewModel.update(note);
            Snackbar.make(findViewById(R.id.activity_content), R.string.note_updated, Snackbar.LENGTH_LONG).show();
        }
    }
}
