package com.devlab74.mynotes;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab74.mynotes.adapters.CategoryDrawerAdapter;
import com.devlab74.mynotes.adapters.NoteRecyclerAdapter;
import com.devlab74.mynotes.models.Category;
import com.devlab74.mynotes.models.Note;
import com.devlab74.mynotes.viewmodels.CategoryViewModel;
import com.devlab74.mynotes.viewmodels.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class NotesActivity extends BaseActivity {

    private NoteViewModel noteViewModel;
    private CategoryViewModel categoryViewModel;

    private static final int ADD_NOTE_REQUEST = 0;
    private static final int EDIT_NOTE_REQUEST = 1;

    private List<Note> notesList;
    private List<Category> categoryList;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private CategoryDrawerAdapter categoryDrawerAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerViewNavigation;
    private ImageButton addCategoryButton;
    private SearchView searchView;
    FrameLayout activityContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        setContentView(R.layout.activity_notes);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(onClickListener);
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        addCategoryButton = headerView.findViewById(R.id.add_category);
        activityContent = findViewById(R.id.activity_content);

        initRecyclerView();
        subscribeObservers();
        initNavigationView();
        initActionBar();
        initSearchView();
    }

    private void subscribeObservers() {
        noteViewModel.getAllNotes().observe(this, notes -> {
            notesList = notes;
            noteRecyclerAdapter.submitList(notesList);
            noteRecyclerAdapter.setNotes(notesList);
        });

        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;
            categoryDrawerAdapter.setCategories(categories);
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
            intent.putExtra(AddEditNoteActivity.EXTRA_CATEGORY_TITLE, note.getCategoryTitle());
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
                Snackbar.make(activityContent, R.string.note_deleted, Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && floatingActionButton.isShown())
                    floatingActionButton.hide();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private View.OnClickListener onClickListener = view -> {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    };

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
    }

    private void initNavigationView() {
        recyclerViewNavigation = navigationView.findViewById(R.id.recycler_view_navigation);
        recyclerViewNavigation.setLayoutManager(new LinearLayoutManager(this));
        categoryDrawerAdapter = new CategoryDrawerAdapter();
        recyclerViewNavigation.setAdapter(categoryDrawerAdapter);

        categoryDrawerAdapter.setOnItemClickListener(category -> {
            noteRecyclerAdapter.getNoteFilter().filter("cat:" + category.getTitle());
            drawerLayout.closeDrawers();
        });

        addCategoryButton.setOnClickListener(view -> {
            new LovelyTextInputDialog(this)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle(R.string.new_category_title)
                    .setIcon(R.drawable.ic_add_category)
                    .setConfirmButton(android.R.string.ok, text -> {
                        if (!text.trim().isEmpty()) {
                            for (Category cat : categoryList) {
                                if (cat.getTitle().trim().toLowerCase().equals(text.trim().toLowerCase())) {
                                    Snackbar.make(activityContent, R.string.category_exists, Snackbar.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            categoryViewModel.insert(new Category(text));
                            Snackbar.make(activityContent, R.string.category_added, Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(activityContent, R.string.category_title_cannot_be_empty, Snackbar.LENGTH_LONG).show();
                        }
                    }).show();
        });

        categoryDrawerAdapter.setOnLongClickListener(category -> {
            if (!category.getTitle().equals("All notes") && !category.getTitle().equals("Uncategorized")) {
                new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                        .setTopColorRes(R.color.colorPrimary)
                        .setButtonsColorRes(R.color.colorPrimaryDark)
                        .setTitle(R.string.delete_category)
                        .setIcon(R.drawable.ic_delete)
                        .setMessage(R.string.warning_delete_category)
                        .setPositiveButton(android.R.string.ok, v -> {
                            for (Note n : notesList) {
                                if (n.getCategoryTitle().equals(category.getTitle())) {
                                    noteViewModel.delete(n);
                                }
                            }
                            categoryViewModel.delete(category);
                            Snackbar.make(activityContent, R.string.category_title_cannot_be_empty, Snackbar.LENGTH_LONG).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }

    private void initSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteRecyclerAdapter.getNoteFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String imagePath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);
            String categoryTitle = data.getStringExtra(AddEditNoteActivity.EXTRA_CATEGORY_TITLE);

            Note note = new Note(title, description, dateCreated, dateUpdated, imagePath, categoryTitle);

            noteViewModel.insert(note);
            Snackbar.make(activityContent, R.string.note_saved, Snackbar.LENGTH_LONG).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            Date dateCreated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_CREATED);
            Date dateUpdated = (Date) data.getSerializableExtra(AddEditNoteActivity.EXTRA_DATE_UPDATED);
            String imagePath = data.getStringExtra(AddEditNoteActivity.EXTRA_IMAGE_PATH);
            String categoryTitle = data.getStringExtra(AddEditNoteActivity.EXTRA_CATEGORY_TITLE);

            Note note = new Note(title, description, dateCreated, dateUpdated, imagePath, categoryTitle);

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Snackbar.make(activityContent, R.string.note_not_updated, Snackbar.LENGTH_LONG).show();
                return;
            }

            note.setId(id);
            noteViewModel.update(note);
            Snackbar.make(activityContent, R.string.note_updated, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }
}
