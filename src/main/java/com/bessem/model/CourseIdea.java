package com.bessem.model;

import com.github.slugify.Slugify;

import java.util.*;

public class CourseIdea {

    private String title;
    private String author;
    private String slug;
    private Set<String> voters;

    public CourseIdea(String title, String author) {
        this.title = title;
        this.author = author;
        this.voters = new HashSet<>();
        Slugify slugify = new Slugify();
        slug = slugify.slugify(title);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSlug() {
        return slug;
    }

    public boolean vote(String username) {
        return voters.add(username);
    }

    public int getVoteCount() {
        return voters.size();
    }

    public List<String> getVoters() {
        return new ArrayList<>(voters);
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
