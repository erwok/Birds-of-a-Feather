package com.example.birdsofafeather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "BoaF_Home";
    public static final int USER_ID = 0;

    private Message publishedMessage;
    protected RecyclerView matchedStudentsView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentsViewAdapter studentsViewAdapter;
    private MessageListener messageListener;
    private AppDatabase db;

    protected StudentWithCourses user;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = AppDatabase.singleton(getApplicationContext());

        //FOR TESTING STORY 8
        db.clearAllTables();
        Student user_temp = new Student(USER_ID,"Daniel", "");
        user_temp.isUser = true;
        Student friend1 = new Student(1, "Elizabeth", "");
        Student friend2 = new Student(2, "Rye", "");
        Student friend3 = new Student(3, "Jeff", "");
        Student friend4 = new Student(4, "Helen", "");
        Student friend5 = new Student(5, "Eric", "");

        db.studentWithCoursesDao().insert(user_temp);
        db.studentWithCoursesDao().insert(friend1);
        db.studentWithCoursesDao().insert(friend2);
        db.studentWithCoursesDao().insert(friend3);
        db.studentWithCoursesDao().insert(friend4);
        db.studentWithCoursesDao().insert(friend5);
        db.coursesDao().insert(new Course(USER_ID, 2021, "FA", "CSE", "100"));
        db.coursesDao().insert(new Course(USER_ID, 2021, "FA", "CSE", "110"));
        db.coursesDao().insert(new Course(USER_ID, 2020, "SP", "CSE", "101"));
        db.coursesDao().insert(new Course(USER_ID, 2020, "SP", "MATH", "20A"));

        db.coursesDao().insert(new Course(1, 2021, "FA", "CSE", "100"));
        db.coursesDao().insert(new Course(1, 2021, "FA", "CSE", "110"));

        db.coursesDao().insert(new Course(4, 2021, "FA", "CSE", "100"));
        db.coursesDao().insert(new Course(4, 2020, "SP", "CSE", "101"));
        db.coursesDao().insert(new Course(4, 2020, "SP", "PHIL", "27"));

        Log.d(TAG, "Student 1 name: " + db.studentWithCoursesDao().get(1).getName());
        Log.d(TAG, "Student 1 isUser: " + db.studentWithCoursesDao().get(1).student.isUser);

        // END OF TESTING
        user = db.studentWithCoursesDao().getUser();
        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents(); // May not be sorted yet, we do that later
        this.publishedMessage = new Message(user.toByteArray());

        // Calculate number of shared courses
        for (StudentWithCourses student : students) {
            student.calculateSharedCourseCount(user);
            db.studentWithCoursesDao().updateStudent(student.student);
        }

        matchedStudentsView = findViewById(R.id.matched_students_view);
        studentsLayoutManager = new LinearLayoutManager(getApplicationContext());
        matchedStudentsView.setLayoutManager(studentsLayoutManager);

        studentsViewAdapter = new StudentsViewAdapter(db.studentWithCoursesDao().getSortedOtherStudents());
        matchedStudentsView.setAdapter(studentsViewAdapter);
    }

    public void onAddCoursesClicked(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    public void onStartClicked(View view) {
        Button start = findViewById(R.id.start_btn);
        start.setVisibility(View.GONE);

        Button stop = findViewById(R.id.stop_btn);
        stop.setVisibility(View.VISIBLE);

        MessageListener realListener = new MessageListener() {
            //put information into database
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message!");
                StudentWithCourses foundStudent;
                // Make sure we received a valid message.
                try {
                    foundStudent = new StudentWithCourses(db.studentWithCoursesDao().count() + 1, message.getContent());
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Received invalid message: " + e.getLocalizedMessage());
                    return;
                }
                foundStudent.calculateSharedCourseCount(user);
                db.studentWithCoursesDao().insert(foundStudent.student);
                for(String courseTitle : foundStudent.courses) {
                    db.coursesDao().insert(new Course(courseTitle, foundStudent.getId()));
                }

                Log.d(TAG, "New otherStudents size: " + db.studentWithCoursesDao().getSortedOtherStudents());
                studentsViewAdapter.addStudent(db.studentWithCoursesDao().getSortedOtherStudents());
            }
        };

        // Build a fake student
        StudentWithCourses fakedMessageStudent = new StudentWithCourses();
        fakedMessageStudent.student = new Student(0, "Jacob", "https://cdn.wccftech.com/wp-content/uploads/2017/07/nearby_connections.png");
        fakedMessageStudent.courses.add(new Course(0, 2021, "FA", "CSE", "110").courseTitle);

        //eventually not faked
        this.messageListener = new FakedMessageListener(realListener, 10, fakedMessageStudent.toByteArray());
        Nearby.getMessagesClient(this).subscribe(messageListener);
        Nearby.getMessagesClient(this).publish(publishedMessage);
    }

    public void onStopClicked(View view) {
        Button start = findViewById(R.id.start_btn);
        start.setVisibility(View.VISIBLE);

        Button stop = findViewById(R.id.stop_btn);
        stop.setVisibility(View.GONE);

        ((FakedMessageListener) messageListener).stopExecutor();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        Nearby.getMessagesClient(this).unpublish(this.publishedMessage);
    }
}