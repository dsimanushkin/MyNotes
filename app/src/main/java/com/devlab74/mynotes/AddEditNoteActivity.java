package com.devlab74.mynotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devlab74.mynotes.models.Category;
import com.devlab74.mynotes.viewmodels.CategoryViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddEditNoteActivity extends BaseActivity {

    public static final String EXTRA_ID = "com.devlab74.mynotes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.devlab74.mynotes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.devlab74.mynotes.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE_CREATED = "com.devlab74.mynotes.EXTRA_DATE_CREATED";
    public static final String EXTRA_DATE_UPDATED = "com.devlab74.mynotes.EXTRA_DATE_UPDATED";
    public static final String EXTRA_IMAGE_PATH = "com.devlab74.mynotes.EXTRA_IMAGE_PATH";
    public static final String EXTRA_CATEGORY_TITLE = "com.devlab74.mynotes.EXTRA_CATEGORY_TITLE";
    private static final int PICK_IMAGE = 1;

    private TextView toolbarTitle;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private ImageView noteOptionalPhoto;
    private Uri imagePath;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.note_title);
        editTextDescription = findViewById(R.id.note_description);
        noteOptionalPhoto = findViewById(R.id.note_optional_image);
        categorySpinner = findViewById(R.id.category_spinner);

        initActionBar();
        subscribeObservers();
        setupNote();
    }

    private void subscribeObservers() {
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            ArrayList<Category> categoriesFinal = new ArrayList<>(categories);
            categoriesFinal.remove(0);
            ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesFinal);
            categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryArrayAdapter);
            Intent intent = getIntent();
            if (!categoriesFinal.isEmpty() && intent.hasExtra(EXTRA_CATEGORY_TITLE)) {
                String categoryTitle = intent.getStringExtra(EXTRA_CATEGORY_TITLE);
                for (int i = 0; i < categoriesFinal.size(); i++) {
                    if (categoriesFinal.get(i).getTitle().equals(categoryTitle)) {
                        categorySpinner.setSelection(i);
                        return;
                    }
                }
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        SearchView searchViewToolbar = toolbar.findViewById(R.id.search_view);
        searchViewToolbar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED) {
            MenuItem item = menu.findItem(R.id.note_menu_add_image);
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.note_menu_save: {
                saveNote();
                break;
            }
            case R.id.note_menu_add_image: {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            }
        }
        return true;
    }

    public Category getSelectedCategory() {
        return (Category) categorySpinner.getSelectedItem();
    }

    private void setupNote() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            toolbarTitle.setText(R.string.edit_note);
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            if (intent.getStringExtra(EXTRA_IMAGE_PATH) != null) {
                imagePath = Uri.parse(getIntent().getStringExtra(EXTRA_IMAGE_PATH));
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.white_background)
                        .error(R.drawable.white_background);
                Glide.with(this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(imagePath)
                        .into(noteOptionalPhoto);
                viewAddedImage();
            }
        } else {
            toolbarTitle.setText(R.string.add_note);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Date currentDate = Calendar.getInstance().getTime();
        Date dateCreated = (Date) getIntent().getSerializableExtra(EXTRA_DATE_CREATED);
        String categoryTitle = getSelectedCategory().getTitle();
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Snackbar.make(findViewById(R.id.activity_content), R.string.note_cannot_be_empty, Snackbar.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_CATEGORY_TITLE, categoryTitle);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
            intent.putExtra(EXTRA_DATE_CREATED, dateCreated);
        } else {
            intent.putExtra(EXTRA_DATE_CREATED, currentDate);
        }
        intent.putExtra(EXTRA_DATE_UPDATED, currentDate);
        if (imagePath != null) {
            intent.putExtra(EXTRA_IMAGE_PATH, imagePath.toString());
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void viewAddedImage() {
        noteOptionalPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViewPhotoActivity.class);
            intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            noteOptionalPhoto.setVisibility(View.VISIBLE);
            imagePath = data.getData();

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.white_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(imagePath)
                    .into(noteOptionalPhoto);

            viewAddedImage();
        }
    }
}
