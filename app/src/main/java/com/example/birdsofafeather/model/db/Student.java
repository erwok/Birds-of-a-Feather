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

    public Student(int studentId, String name, String photoURL) {
        this.studentId = studentId;
        this.name = name;
        this.photoURL = photoURL;
    }
}
