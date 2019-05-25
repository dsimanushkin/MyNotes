package com.devlab74.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddEditNoteActivity extends BaseActivity {

    public static final String EXTRA_ID = "com.devlab74.mynotes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.devlab74.mynotes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.devlab74.mynotes.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE_CREATED = "com.devlab74.mynotes.EXTRA_DATE_CREATED";
    public static final String EXTRA_DATE_UPDATED = "com.devlab74.mynotes.EXTRA_DATE_UPDATED";

    private TextView toolbarTitle;
    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        initActionBar();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
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
        }
        return true;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Date currentDate = Calendar.getInstance().getTime();
        Date dateCreated = null;
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, R.string.note_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
            intent.putExtra(EXTRA_DATE_CREATED, dateCreated);
        } else {
            intent.putExtra(EXTRA_DATE_CREATED, currentDate);
        }
        intent.putExtra(EXTRA_DATE_UPDATED, currentDate);
        setResult(RESULT_OK, intent);
        finish();
    }
}
