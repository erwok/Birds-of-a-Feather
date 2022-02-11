package com.example.birdsofafeather.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    public int courseId = 0;

    @ColumnInfo(name = "student_id")
    public int studentId;

    @ColumnInfo(name="year")
    public int year;

    @ColumnInfo(name="quarter")
    public String quarter;

    @ColumnInfo(name="subject")
    public String subject;

    @ColumnInfo(name="course_num")
    public int courseNum;

    @ColumnInfo(name = "course_title")
    public String courseTitle;

    public Course(int studentId, int year, String quarter, String subject, int courseNum) {
        this.studentId = studentId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNum = courseNum;
        this.courseTitle = "" + year + " " + quarter + " " + subject + " " + courseNum;
    }

    @Ignore // So room doesn't get confused and try to use this constructor
    public Course(String courseTitle, int studentId) {
        this.courseTitle = courseTitle;
        String[] splitTitle = this.courseTitle.split(" ");
        this.studentId = studentId;
        this.year = Integer.parseInt(splitTitle[0]);
        this.quarter = splitTitle[1];
        this.subject = splitTitle[2];
        this.courseNum = Integer.parseInt(splitTitle[3]);
    }
}
