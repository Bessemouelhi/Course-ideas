package com.bessem.model;

import java.util.List;

public interface CourseIdeaDao {
    void add(CourseIdea idea);
    List<CourseIdea> getAll();
}
