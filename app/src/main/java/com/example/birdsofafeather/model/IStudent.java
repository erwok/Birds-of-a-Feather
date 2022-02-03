package com.example.birdsofafeather.model;

import com.example.birdsofafeather.model.db.Course;

import java.util.List;

public interface IStudent {
    public int getId();
    public String getName();
    public String getPhotoURL();
    public abstract List<String> getClasses();
}
