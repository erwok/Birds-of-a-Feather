package com.example.birdsofafeather.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.model.IStudent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;

public class StudentWithCourses implements IStudent {
    @Embedded
    public Student student;

    @Relation(parentColumn = "id",
            entityColumn = "student_id",
            entity = Course.class,
            projection = {"course_title"}
    )
    public List<String> courses;

    @Override
    public int getId() { return this.student.studentId; }

    @Override
    public String getName() { return this.student.name; }

    @Override
    public String getPhotoURL() { return this.student.photoURL; }

    @Override
    public List<String> getClasses() { return this.courses; }

    public byte[] toByteArray() {
        byte[] nameBytes = student.name.getBytes(StandardCharsets.US_ASCII);
        byte[] photoBytes = student.photoURL.getBytes(StandardCharsets.US_ASCII);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(nameBytes, 0, nameBytes.length);
        outputStream.write(0);
        outputStream.write(photoBytes, 0, photoBytes.length);
        outputStream.write(0);
        for (String course : this.courses) {
            byte[] course_bytes = course.getBytes(StandardCharsets.US_ASCII);
            outputStream.write(course_bytes, 0, course_bytes.length);
            outputStream.write(0);
        }
        return outputStream.toByteArray();
    }
}
