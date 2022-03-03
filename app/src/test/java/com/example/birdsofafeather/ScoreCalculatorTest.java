package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentSorter;
import com.example.birdsofafeather.model.db.StudentWithCourses;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class ScoreCalculatorTest {
    AppDatabase db;
    StudentSorter sorter;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
        sorter = new StudentSorter(db);
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testNoCommonCourses() {
        Student user = new Student(0, "Default User", "", UUID.randomUUID().toString());
        user.isUser = true;
        db.studentWithCoursesDao().insert(user);

        Course userCourse1 = new Course(0, 2022, "WI", "CSE",
                "110", 3);
        Course userCourse2 = new Course(0, 2022, "WI", "CSE",
                "132", 0);
        db.coursesDao().insert(userCourse1);
        db.coursesDao().insert(userCourse2);

        Student other = new Student(1, "Other Student", "", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(other);

        Course otherCourse1 = new Course(1, 2021, "WI", "CSE",
                "110", 0);
        Course otherCourse2 = new Course(1, 2022, "WI", "MGT",
                "181", 0);
        db.coursesDao().insert(otherCourse1);
        db.coursesDao().insert(otherCourse2);

        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        StudentWithCourses stud = students.get(0);

        assertEquals(0, sorter.calculateSharedCourseCount(stud));
        assertEquals(0, sorter.calculateSizeScore(stud));
        assertEquals(0, sorter.calculateRecencyScore(stud));
        assertEquals(0, sorter.calculateThisQuarterScore(stud));
    }

    @Test
    public void testCalculateSharedCourseCount() {
        Student user = new Student(0, "Default User", "", UUID.randomUUID().toString());
        user.isUser = true;
        db.studentWithCoursesDao().insert(user);
        Course uc1 = new Course(0, 2022, "WI", "CSE",
                "110", 3);
        Course uc2 = new Course(0, 2022, "WI", "CSE",
                "132", 0);
        db.coursesDao().insert(uc1);
        db.coursesDao().insert(uc2);

        Student s1 = new Student(1, "Student 1", "", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(s1);
        Course s1c1 = new Course(1, 2022, "WI", "CSE",
                "110", 3);
        Course s1c2 = new Course(1, 2022, "WI", "MGT",
                "181", 0);
        Course s1c3 = new Course(1, 2022, "WI", "CSE",
                "132", 0);
        db.coursesDao().insert(s1c1);
        db.coursesDao().insert(s1c2);
        db.coursesDao().insert(s1c3);

        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        StudentWithCourses stud1 = students.get(0);

        assertEquals(2, sorter.calculateSharedCourseCount(stud1));
    }

    @Test
    public void testCalculateThisQuarterScore() {
        Student user = new Student(0, "Default User", "", UUID.randomUUID().toString());
        user.isUser = true;
        db.studentWithCoursesDao().insert(user);
        Course uc1 = new Course(0, 2022, "WI", "CSE",
                "110", 3);
        Course uc2 = new Course(0, 2022, "WI", "CSE",
                "132", 0);
        Course uc3 = new Course(0, 2021, "WI", "CSE",
                "132", 0);
        db.coursesDao().insert(uc1);
        db.coursesDao().insert(uc2);
        db.coursesDao().insert(uc3);

        Student s1 = new Student(1, "Student 1", "", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(s1);
        Course sc1 = new Course(1, 2022, "WI", "CSE",
                "110", 3);
        Course sc2 = new Course(1, 2022, "WI", "CSE",
                "132", 0);
        Course sc3 = new Course(1, 2021, "WI", "CSE",
                "132", 0);
        db.coursesDao().insert(sc1);
        db.coursesDao().insert(sc2);
        db.coursesDao().insert(sc3);

        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        StudentWithCourses stud1 = students.get(0);

        assertEquals(2, sorter.calculateThisQuarterScore(stud1));
    }

    @Test
    public void testCalculateSizeScore() {
        Student user = new Student(0, "Default User", "", UUID.randomUUID().toString());
        user.isUser = true;
        db.studentWithCoursesDao().insert(user);
        Course uc1 = new Course(0, 2022, "WI", "CSE",
                "110", 0);
        Course uc2 = new Course(0, 2022, "WI", "CSE",
                "132", 1);
        Course uc3 = new Course(0, 2021, "WI", "MGT",
                "181", 2);
        Course uc4 = new Course(0, 2021, "WI", "MGT",
                "112", 3);
        Course uc5 = new Course(0, 2020, "FA", "CSE",
                "142L", 4);
        Course uc6 = new Course(0, 2021, "WI", "CSE",
                "101", 5);
        db.coursesDao().insert(uc1);
        db.coursesDao().insert(uc2);
        db.coursesDao().insert(uc3);
        db.coursesDao().insert(uc4);
        db.coursesDao().insert(uc5);
        db.coursesDao().insert(uc6);

        Student s1 = new Student(1, "Student 1", "", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(s1);
        Course sc1 = new Course(1, 2022, "WI", "CSE",
                "110", 0);
        Course sc2 = new Course(1, 2022, "WI", "CSE",
                "132", 1);
        Course sc3 = new Course(1, 2021, "WI", "MGT",
                "181", 2);
        Course sc4 = new Course(1, 2021, "WI", "MGT",
                "112", 3);
        Course sc5 = new Course(1, 2020, "FA", "CSE",
                "142L", 4);
        Course sc6 = new Course(1, 2021, "WI", "CSE",
                "101", 5);
        Course sc7 = new Course(1, 2021, "WI", "WCWP",
                "10A", 0);
        db.coursesDao().insert(sc1);
        db.coursesDao().insert(sc2);
        db.coursesDao().insert(sc3);
        db.coursesDao().insert(sc4);
        db.coursesDao().insert(sc5);
        db.coursesDao().insert(sc6);
        db.coursesDao().insert(sc7);

        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        StudentWithCourses stud1 = students.get(0);

        assertEquals(170, sorter.calculateSizeScore(stud1));
    }

    @Test
    public void testCalculateRecencyScore(){
        Student user = new Student(0, "Default User", "", UUID.randomUUID().toString());
        user.isUser = true;
        db.studentWithCoursesDao().insert(user);
        Course uc1 = new Course(0, 2022, "WI", "CSE",
                "110", 0);
        Course uc2 = new Course(0, 2021, "FA", "CSE",
                "132", 1);
        Course uc3 = new Course(0, 2021, "SSS", "MGT",
                "181", 2);
        Course uc4 = new Course(0, 2021, "SP", "MGT",
                "112", 3);
        Course uc5 = new Course(0, 2021, "WI", "CSE",
                "142L", 4);
        Course uc6 = new Course(0, 2020, "FA", "CSE",
                "101", 5);
        db.coursesDao().insert(uc1);
        db.coursesDao().insert(uc2);
        db.coursesDao().insert(uc3);
        db.coursesDao().insert(uc4);
        db.coursesDao().insert(uc5);
        db.coursesDao().insert(uc6);

        Student s1 = new Student(1, "Student 1", "", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(s1);
        Course sc1 = new Course(1, 2022, "WI", "CSE",
                "110", 0);
        Course sc2 = new Course(1, 2021, "FA", "CSE",
                "132", 1);
        Course sc3 = new Course(1, 2021, "SSS", "MGT",
                "181", 2);
        Course sc4 = new Course(1, 2021, "SP", "MGT",
                "112", 3);
        Course sc5 = new Course(1, 2021, "WI", "CSE",
                "142L", 4);
        Course sc6 = new Course(1, 2020, "FA", "CSE",
                "101", 5);
        db.coursesDao().insert(sc1);
        db.coursesDao().insert(sc2);
        db.coursesDao().insert(sc3);
        db.coursesDao().insert(sc4);
        db.coursesDao().insert(sc5);
        db.coursesDao().insert(sc6);

        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        StudentWithCourses stud1 = students.get(0);

        assertEquals(5 + 5 + 4 + 3 + 2 + 1, sorter.calculateRecencyScore(stud1));
    }
}
