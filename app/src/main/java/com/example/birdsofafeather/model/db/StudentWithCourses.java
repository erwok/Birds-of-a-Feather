package com.example.birdsofafeather.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.model.IStudent;

import java.util.HashSet;
import java.util.List;

public class StudentWithCourses implements IStudent {
    @Embedded
    public Student student;

    @Relation(parentColumn = "id",
            entityColumn = "student_id",
            entity = Course.class,
            projection = {"course_title"}
    )
    public List<String> courses;

    @Override
    public int getId() { return this.student.studentId; }

    @Override
    public String getName() { return this.student.name; }

    @Override
    public String getPhotoURL() { return this.student.photoURL; }

    @Override
    public List<String> getClasses() { return this.courses; }
}
