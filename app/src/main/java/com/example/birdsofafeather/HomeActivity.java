package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Nearby-Devices";
    private MessageListener messageListener;
    private Message myMessage;
    protected StudentWithCourses meWithCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // For testing
        meWithCourses = new StudentWithCourses();
        meWithCourses.student.name = getPreferences(MODE_PRIVATE).getString("name", "TEST_NAME");
        meWithCourses.student.studentId = 0;
        meWithCourses.student.photoURL = "";
        meWithCourses.courses = new ArrayList<>();
        meWithCourses.courses.add(new Course(0, 2022, "Winter", "CSE", "110").courseTitle);


        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        MessageListener listener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message: " + new String(message.getContent()));
            }
            @Override
            public void onLost(@NonNull Message message) {
                Log.d(TAG, "Lost sight of message: " + new String(message.getContent()));
            }
        };

        myMessage = new Message(meWithCourses.toByteArray());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Nearby.getMessagesClient(this).publish(myMessage);
        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Nearby.getMessagesClient(this).unpublish(myMessage);
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    public void onAddCoursesClicked(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
}