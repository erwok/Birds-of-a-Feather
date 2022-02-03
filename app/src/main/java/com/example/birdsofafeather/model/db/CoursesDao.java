package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CoursesDao {
    @Transaction
    @Query("SELECT * FROM courses WHERE student_id=:studentId")
    List<Course> getForStudent(int studentId);

    @Transaction
    @Query("SELECT * FROM courses")
    List<Course> getCourses();

    @Query("SELECT * FROM courses WHERE course_id=:courseId")
    Course get(int courseId);

    @Query("SELECT COUNT(*) FROM courses")
    int count();

    @Insert
    void insert(Course course);
}
