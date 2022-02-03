package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "students")
public class Student {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int studentId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "photo_URL")
    public String photoURL;
}
