package com.example.birdsofafeather.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birdsofafeather.model.IStudent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public StudentWithCourses(){

    }

    /**
     * Constructs a StudentWithCourses from a byte[]
     * @param studentID The ID to give the newly-made student
     * @param bytes A byte array, with the format specified in toByteArray
     */
    public StudentWithCourses(int studentID, byte[] bytes) {
        student = new Student();
        student.studentId = studentID;

        courses = new ArrayList<>();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        int nameLength = inputStream.read();
        byte[] nameBytes = new byte[nameLength];
        inputStream.read(nameBytes, 0, nameLength);
        student.name = new String(nameBytes, StandardCharsets.US_ASCII);

        int photoURLLength = inputStream.read() << 8;
        photoURLLength += inputStream.read();
        byte[] photoURLBytes = new byte[photoURLLength];
        inputStream.read(photoURLBytes, 0, photoURLLength);
        student.photoURL = new String(photoURLBytes, StandardCharsets.US_ASCII);

        while(inputStream.available() > 0) {
            int courseBytesLength = inputStream.read();
            byte[] courseBytes = new byte[courseBytesLength];
            inputStream.read(courseBytes, 0, courseBytesLength);
            courses.add(new String(courseBytes, StandardCharsets.US_ASCII));
        }
    }

    /**
     * Encodes this StudentWithCourses's info to binary. The format is:
     * 1 byte for nameLength
     * nameLength bytes for student.name, encoded in ASCII
     * 2 bytes for photoBytesLength (MSB first)
     * photoBytesLength bytes for student.photoURL, encoded in ASCII
     *
     * and then any number of courses, where a course is:
     * 1 byte for courseLength
     * courseLength bytes for the course title, encoded in ASCII
     */
    public byte[] toByteArray() {
        byte[] nameBytes = student.name.getBytes(StandardCharsets.US_ASCII);
        byte[] photoBytes = student.photoURL.getBytes(StandardCharsets.US_ASCII);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(nameBytes.length);
        outputStream.write(nameBytes, 0, nameBytes.length);

        // manual implementation of 16-bit int in java lol
        outputStream.write((photoBytes.length & 0xFF00) >> 8);
        outputStream.write(photoBytes.length & 0xFF);

        outputStream.write(photoBytes, 0, photoBytes.length);
        for (String course : this.courses) {
            byte[] course_bytes = course.getBytes(StandardCharsets.US_ASCII);
            outputStream.write(course_bytes.length);
            outputStream.write(course_bytes, 0, course_bytes.length);
        }
        return outputStream.toByteArray();
    }
}
