package com.bessem.model;

import java.util.ArrayList;
import java.util.List;

public class CourseIdeaImpl implements CourseIdeaDao {

    private List<CourseIdea> ideaList;

    public CourseIdeaImpl() {
        ideaList = new ArrayList<>();
    }

    @Override
    public void add(CourseIdea idea) {
        ideaList.add(idea);
    }

    @Override
    public List<CourseIdea> getAll() {
        return new ArrayList<>(ideaList);
    }
}
