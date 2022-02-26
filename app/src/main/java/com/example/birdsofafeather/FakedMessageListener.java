package com.example.birdsofafeather;

import android.content.Context;

import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakedMessageListener extends MessageListener{
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    public FakedMessageListener(MessageListener realMessageListener, int frequency, Context context) {
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {

            StudentWithCourses fakedMessageStudent = new StudentWithCourses();

            try {
                InputStream is = context.getAssets().open("mock.csv");
                Scanner in = new Scanner(is);
                in.useDelimiter(",,,");
                String name = in.next().trim();
                System.out.println(name);
                String photoURL = in.next().trim();
                System.out.println(photoURL);
                String course = in.next().trim();
                String[] course_parts = course.split(",");

                System.out.println(course);
                fakedMessageStudent.student = new Student(0, name, photoURL);
                fakedMessageStudent.courses.add(new Course(0, Integer.parseInt(
                        course_parts[0]), course_parts[1], course_parts[2], course_parts[3],
                        Integer.parseInt(course_parts[4])).courseTitle);
            }
            catch (IOException e) {
                e.printStackTrace();
                stopExecutor();
            }

            byte[] messageBytes = fakedMessageStudent.toByteArray();
            Message message = new Message(messageBytes);
            this.messageListener.onFound(message);
            this.messageListener.onLost(message);

        }, 0, frequency, TimeUnit.SECONDS);
    }

    public void stopExecutor() {
        this.executor.shutdown();
    }
}
