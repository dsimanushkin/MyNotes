package com.devlab74.mynotes.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
    private OnItemClickListener onItemClickListener;

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
                    oldItem.getDateLastUpdated().equals(newItem.getDateLastUpdated()) &&
                    oldItem.getCategoryTitle().equals(newItem.getCategoryTitle());
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

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    private Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filteredNotes = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0 || charSequence == "" || charSequence.equals("cat:All notes")) {
                filteredNotes.addAll(notesFull);
            } else {
                if (charSequence.length() >= 4 && charSequence.subSequence(0, 4).equals("cat:")) {
                    String filterPattern = charSequence.subSequence(4, charSequence.length()).toString();
                    for (Note note : notesFull) {
                        if (note.getCategoryTitle().contains(filterPattern)) {
                            filteredNotes.add(note);
                        }
                    }
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (Note note : notesFull) {
                        if (note.getTitle().toLowerCase().contains(filterPattern)
                                || note.getDescription().toLowerCase().contains(filterPattern)) {
                            filteredNotes.add(note);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredNotes;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notes.clear();
            if (filterResults.values != null) {
                notes.addAll((List) filterResults.values);
            }
            notifyDataSetChanged();
        }
    };

    public Filter getNoteFilter() {
        return noteFilter;
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

            itemView.setOnClickListener(view -> {
                int position = NoteHolder.this.getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
