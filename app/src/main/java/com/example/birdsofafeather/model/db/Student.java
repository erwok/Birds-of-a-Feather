package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int studentId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "photo_URL")
    public String photoURL;

    /**
     * How many courses this student has in common with the user.
     */
    @ColumnInfo(name = "common_courses", defaultValue = "-1")
    public int commonCourses = -1;

    @ColumnInfo(name = "is_user", defaultValue = "FALSE")
    public boolean isUser = false;

    public Student(int studentId, String name, String photoURL) {
        this.studentId = studentId;
        this.name = name;
        this.photoURL = photoURL;
    }
}
