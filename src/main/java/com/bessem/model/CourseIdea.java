package com.bessem.model;

import java.util.Objects;

public class CourseIdea {

    private String title;
    private String author;

    public CourseIdea(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseIdea)) return false;
        CourseIdea that = (CourseIdea) o;
        return getTitle().equals(that.getTitle()) &&
                getAuthor().equals(that.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthor());
    }
}
