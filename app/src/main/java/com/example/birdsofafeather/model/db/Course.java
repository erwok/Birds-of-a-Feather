package com.example.birdsofafeather.model.db;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.nio.charset.StandardCharsets;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey
    @ColumnInfo(name = "course_id")
    public int courseId;

    @ColumnInfo(name = "student_id")
    public int studentId;

    @ColumnInfo(name="year")
    public int year;

    @ColumnInfo(name="quarter")
    public String quarter;

    @ColumnInfo(name="subject")
    public String subject;

    @ColumnInfo(name="course_num")
    public String courseNum;

    @ColumnInfo(name = "course_title")
    public String courseTitle;

    public Course(int courseId, int year, String quarter, String subject, String courseNum) {
        this.courseId = courseId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNum = courseNum;
        this.courseTitle = "" + year + " " + quarter + " " + subject + " " + courseNum;
    }

    public byte[] toByteArray(){
        return courseTitle.getBytes(StandardCharsets.US_ASCII);
    }
}
