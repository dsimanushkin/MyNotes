package com.devlab74.mynotes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date dateCreated;
    private Date dateLastUpdated;
    private String optionalImagePath;

    public Note(String title, String description, Date dateCreated, Date dateLastUpdated, String optionalImagePath) {
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
        this.optionalImagePath = optionalImagePath;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public String getOptionalImagePath() {
        return optionalImagePath;
    }

    public void setOptionalImagePath(String optionalImagePath) {
        this.optionalImagePath = optionalImagePath;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateLastUpdated=" + dateLastUpdated +
                ", optionalImagePath='" + optionalImagePath + '\'' +
                '}';
    }
}
