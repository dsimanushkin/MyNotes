package com.devlab74.mynotes.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devlab74.mynotes.R;
import com.devlab74.mynotes.models.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteRecyclerAdapter extends ListAdapter<Note, NoteRecyclerAdapter.NoteHolder> {

    private Context context;
    private List<Note> notesFull;
    private List<Note> notes = new ArrayList<>();

    public NoteRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getDateCreated().equals(newItem.getDateCreated()) &&
                    oldItem.getDateLastUpdated().equals(newItem.getDateLastUpdated());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        context = parent.getContext();
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.noteTitle.setText(currentNote.getTitle());
        holder.noteDescription.setText(currentNote.getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        if (currentNote.getDateLastUpdated() != null && currentNote.getDateCreated() != null) {
            String dateCreated = dateFormat.format(currentNote.getDateCreated());
            String dateUpdated = dateFormat.format(currentNote.getDateLastUpdated());
            String date = dateCreated + " / " + dateUpdated;
            holder.noteDate.setText(date);
        }

        if (currentNote.getOptionalImagePath() != null) {
            Uri uri = Uri.parse(currentNote.getOptionalImagePath());
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.white_background);
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(uri)
                    .into(holder.noteOptionalPhoto);
        } else {
            holder.noteOptionalPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (notes.size() > 0) {
            return notes.size();
        }
        return 0;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notesFull = new ArrayList<>(notes);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle;
        private TextView noteDescription;
        private TextView noteDate;
        private ImageView noteOptionalPhoto;


        private NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDescription = itemView.findViewById(R.id.note_description);
            noteDate = itemView.findViewById(R.id.note_date);
            noteOptionalPhoto = itemView.findViewById(R.id.note_optional_image);
        }
    }

}
