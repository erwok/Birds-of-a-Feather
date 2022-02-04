package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class overlappingClassesTest {

    @Test
    public void overlappingTest() {
        Course student1course1 = new Course(0, 2022, "Winter", "CSE", "110");
        Course student1course2 = new Course(1, 2021, "Fall", "TDAC", "1");
        Course student1course3 = new Course(2, 2021, "Fall", "MATH", "20A");

        Student student1 = new Student();
        student1.studentId = 0;
        student1.name = "Rye Gleason";
        student1.photoURL = "https://variety.com/wp-content/uploads/2021/07/Rick-Astley-Never-Gonna-Give-You-Up.png";

        StudentWithCourses studentWithCourses1 = new StudentWithCourses();
        studentWithCourses1.student = student1;
        studentWithCourses1.courses = new ArrayList<>();
        studentWithCourses1.courses.add(student1course1.courseTitle);
        studentWithCourses1.courses.add(student1course2.courseTitle);
        studentWithCourses1.courses.add(student1course3.courseTitle);

        Course student2course1 = new Course(0, 2022, "Winter", "CSE", "110");
        Course student2course2 = new Course(1, 2021, "Fall", "COMM", "10");
        Course student2course3 = new Course(2, 2021, "Fall", "MATH", "20A");

        Student student2 = new Student();
        student2.studentId = 0;
        student2.name = "Rye Gleason";
        student2.photoURL = "https://variety.com/wp-content/uploads/2021/07/Rick-Astley-Never-Gonna-Give-You-Up.png";

        StudentWithCourses studentWithCourses2 = new StudentWithCourses();
        studentWithCourses2.student = student2;
        studentWithCourses2.courses = new ArrayList<>();
        studentWithCourses2.courses.add(student2course1.courseTitle);
        studentWithCourses2.courses.add(student2course2.courseTitle);
        studentWithCourses2.courses.add(student2course3.courseTitle);

        List<String> newList = studentWithCourses1.overlappingClasses(studentWithCourses2);
        assertEquals(2, newList.size());
        assertTrue(newList.contains(student1course1.courseTitle));
        assertTrue(newList.contains(student1course3.courseTitle));

    }
}
