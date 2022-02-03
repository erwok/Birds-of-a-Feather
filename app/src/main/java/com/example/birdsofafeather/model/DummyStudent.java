package com.example.birdsofafeather.model;

import java.util.Arrays;
import java.util.List;

public class DummyStudent implements IStudent {
    private final int id;
    private final String name;
    private final String photoURL;
    private final List<String> courses;

    public DummyStudent(int id, String name, String photoURL, String[] courses) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.courses = Arrays.asList(courses);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhotoURL() {
        return photoURL;
    }

    @Override
    public List<String> getClasses() {
        return courses;
    }
}
