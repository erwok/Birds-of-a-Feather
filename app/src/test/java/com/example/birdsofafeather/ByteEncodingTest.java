package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.example.birdsofafeather.model.db.Wave;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class ByteEncodingTest {

    @Test
    public void checkStudentEncodeDecode() {
        Course course1 = new Course(0, 2022, "Winter", "CSE", "110");
        Course course2 = new Course(1, 2021, "Fall", "COMM", "10");
        Course course3 = new Course(2, 2021, "Fall", "MATH", "20A");

        Student student = new Student(0, "Rye Gleason", "https://variety.com/wp-content/uploads/2021/07/Rick-Astley-Never-Gonna-Give-You-Up.png", UUID.randomUUID().toString());

        StudentWithCourses studentWithCourses = new StudentWithCourses();
        studentWithCourses.student = student;
        studentWithCourses.courses = new ArrayList<>();
        studentWithCourses.courses.add(course1.courseTitle);
        studentWithCourses.courses.add(course2.courseTitle);
        studentWithCourses.courses.add(course3.courseTitle);

        byte[] encodedStudentWithCourses = studentWithCourses.toByteArray();
        StudentWithCourses decoded = new StudentWithCourses(0, encodedStudentWithCourses);

        assertEquals(student.name, decoded.student.name);
        assertEquals(student.photoURL, decoded.student.photoURL);
        assertEquals(course1.courseTitle, decoded.courses.get(0));
        assertEquals(course2.courseTitle, decoded.courses.get(1));
        assertEquals(course3.courseTitle, decoded.courses.get(2));
    }

    @Test
    public void checkWaveEncodeDecode() {
        String uuid = UUID.randomUUID().toString();

        Wave wave = new Wave(uuid, true);
        byte[] waveBytes = wave.toByteArray();
        Wave decodeWave = new Wave(waveBytes);
        assertEquals(uuid, decodeWave.uuid);
        assertEquals(true, decodeWave.waveAt);

        Wave remWave = new Wave(uuid, false);
        byte[] remWaveBytes = remWave.toByteArray();
        Wave decodeRemWave = new Wave(remWaveBytes);
        assertEquals(uuid, decodeRemWave.uuid);
        assertEquals(false, decodeRemWave.waveAt);
    }
}
