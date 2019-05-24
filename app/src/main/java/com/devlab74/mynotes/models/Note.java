package com.devlab74.mynotes.models;

import java.util.Date;

public class Note {

    private String title;
    private String description;
    private Date dateCreated;
    private Date dateLastUpdated;

    public Note(String title, String description, Date dateCreated, Date dateLastUpdated) {
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
    }

    public Note() {
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

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateLastUpdated=" + dateLastUpdated +
                '}';
    }
}
