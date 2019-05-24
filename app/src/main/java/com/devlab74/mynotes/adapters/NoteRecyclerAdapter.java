package com.devlab74.mynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devlab74.mynotes.R;
import com.devlab74.mynotes.models.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteHolder> {

    private List<Note> notesFull;
    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

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
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView noteTitle;
        private TextView noteDescription;
        private TextView noteDate;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDescription = itemView.findViewById(R.id.note_description);
            noteDate = itemView.findViewById(R.id.note_date);
        }
    }

}
