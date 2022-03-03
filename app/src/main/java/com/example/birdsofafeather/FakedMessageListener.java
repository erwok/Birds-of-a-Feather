package com.example.birdsofafeather;

import android.content.Context;
import android.print.PrinterInfo;

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
import java.util.UUID;

public class FakedMessageListener extends MessageListener{
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    public FakedMessageListener(MessageListener realMessageListener, int frequency, Context context) {
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {

            StudentWithCourses fakedMessageStudent = new StudentWithCourses();

            fakedMessageStudent.student = new Student(0, "Bill",
                    "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3" +
                            "PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbA" +
                            "RsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854" +
                            "-h924-no?authuser=02022", UUID.randomUUID().toString());
            fakedMessageStudent.courses.add(new Course(0, 2021, "FA", "CSE", "110", 5)
                    .courseTitle);

            byte[] messageBytes = fakedMessageStudent.toByteArray();
            Message message = new Message(messageBytes);
            this.messageListener.onFound(message);
            //this.messageListener.onLost(message);

        }, 0, frequency, TimeUnit.SECONDS);
    }

    public void stopExecutor() {
        this.executor.shutdown();
    }
}
