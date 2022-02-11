package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentWithCoursesDao {

    @Query("SELECT * FROM students WHERE is_user LIMIT 1")
    StudentWithCourses getUser();

    @Transaction
    @Query("SELECT * FROM students WHERE is_user='FALSE' ORDER BY common_courses DESC")
    List<StudentWithCourses> getSortedOtherStudents();

    @Query("SELECT * FROM students WHERE id=:id")
    StudentWithCourses get(int id);

    @Query("SELECT COUNT(*) FROM students")
    int count();

    @Insert
    void insert(Student student);

    @Query("UPDATE students SET common_courses='-1'")
    void resetSharedCourses();

    @Update()
    void updateStudent(Student student);
}
