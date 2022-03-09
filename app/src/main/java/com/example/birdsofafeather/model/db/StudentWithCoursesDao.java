package com.example.birdsofafeather.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudentWithCoursesDao {

    /**
     * @return The StudentWithCourses representing this app's user.
     */
    @Query("SELECT * FROM students WHERE is_user LIMIT 1")
    @Transaction
    StudentWithCourses getUser();

    /**
     * @return A list of all non-user students, sorted by number of courses shared with the user,
     * in descending order.
     */
    @Transaction
    @Query("SELECT * FROM students WHERE session=:sessionID AND NOT is_user ORDER BY common_courses DESC")
    List<StudentWithCourses> getSortedOtherStudents(int sessionID);

    @Transaction
    @Query("SELECT * FROM students WHERE session=:sessionID AND NOT is_user ORDER BY this_quarter_score DESC")
    List<StudentWithCourses> getSortedOtherStudentsByThisQuarter(int sessionID);

    @Transaction
    @Query("SELECT * FROM students WHERE session=:sessionID AND NOT is_user ORDER BY size_score DESC")
    List<StudentWithCourses> getSortedOtherStudentsByCourseSize(int sessionID);

    @Transaction
    @Query("SELECT * FROM students WHERE session=:sessionID AND NOT is_user ORDER BY recency_score DESC")
    List<StudentWithCourses> getSortedOtherStudentsByRecency(int sessionID);

    /**
     * @return A list of all non-user students, that have been favorited
     */
    @Transaction
    @Query("SELECT * FROM students WHERE favorite")
    List<StudentWithCourses> getFavoritedStudents();

    @Query("SELECT * FROM students WHERE id=:id")
    @Transaction
    StudentWithCourses get(int id);

    @Query("SELECT * FROM students WHERE UUID=:UUID")
    StudentWithCourses getWithUUID(String UUID);

    @Query("SELECT COUNT(*) FROM students")
    int count();

    @Query("SELECT * FROM students WHERE session=:sessionID AND wave_to_me=:wavedToMe AND NOT is_user")
    List<StudentWithCourses> getStudentsWhoWaved(boolean wavedToMe, int sessionID);

    @Insert
    void insert(Student student);

    @Transaction
    @Query("UPDATE students SET favorite=:favorite WHERE id=:id")
    void favoriteStudent(int id, boolean favorite);

    /**
     * Reset the stored number of courses shared with the user for all saved students. Useful when
     * the user's course list changes, invaliding all the stored common_courses data.
     */
    @Query("UPDATE students SET common_courses='-1'")
    void resetSharedCourses();

    /**
     * Update the stored db info on a student. Mostly useful for after calculating how many courses
     * a student shares with the user.
     * @param student The student object with updated data.
     */
    @Update()
    void updateStudent(Student student);
}
