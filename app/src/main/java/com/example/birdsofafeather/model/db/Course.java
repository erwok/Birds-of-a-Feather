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

    // String because of 20A, 20B, etc. classes
    @ColumnInfo(name="course_num")
    public String courseNum;

    @ColumnInfo(name = "course_title")
    public String courseTitle;

    @ColumnInfo(name = "course_size", defaultValue = "5")
    public int courseSize;

    public Course(int studentId, int year, String quarter, String subject, String courseNum,
                  int courseSize) {
        this.studentId = studentId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNum = courseNum;
        this.courseSize = courseSize;
        this.courseTitle = "" + year + " " + quarter + " " + subject + " " + courseNum;
    }

    /**
     * Construct a course from a courseTitle
     * @param courseTitle The course title, formatted as "year quarter subject courseNum"
     * @param studentId The ID for the student who took this course.
     */
    @Ignore // So room doesn't get confused and try to use this constructor
    public Course(String courseTitle, int studentId) {
        String[] splitTitle = this.courseTitle.split(" ");
        this.studentId = studentId;
        this.year = Integer.parseInt(splitTitle[0]);
        this.quarter = splitTitle[1];
        this.subject = splitTitle[2];
        this.courseNum = splitTitle[3];
        this.courseTitle = "" + this.year + " " + this.quarter + " " + this.subject + " " + this.courseNum;
        this.courseSize = Integer.parseInt(splitTitle[4]);
    }
}
