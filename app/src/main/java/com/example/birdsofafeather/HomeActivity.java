package com.example.birdsofafeather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentSorter;
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

    protected Spinner prioritySpinner;
    protected StudentSorter studentSorter;

    protected StudentWithCourses user;
    private boolean first = true;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = AppDatabase.singleton(getApplicationContext());
        studentSorter = new StudentSorter(db);

        //FOR TESTING STORY 8
        //if (db.studentWithCoursesDao().count() < 2) {
            //Student friend1 = new Student(1, "Elizabeth", "https://ichef.bbci.co.uk/news/976/cpsprodpb/67CF/production/_108857562_mediaitem108857561.jpg");
//            Student friend2 = new Student(2, "Rye", "https://upload.wikimedia.org/wikipedia/commons/7/79/Ear_of_rye.jpg");
//            Student friend3 = new Student(3, "Jeff", "");
//            Student friend4 = new Student(4, "Helen", "https://skynursery.com/wp-content/uploads/2015/01/misc_Annas_Hummingbird_39089535924.jpg");
//            Student friend5 = new Student(5, "Eric", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/204px-Android_robot.svg.png");

            //db.studentWithCoursesDao().insert(friend1);
//            db.studentWithCoursesDao().insert(friend2);
//            db.studentWithCoursesDao().insert(friend3);
//            db.studentWithCoursesDao().insert(friend4);
//            db.studentWithCoursesDao().insert(friend5);

            //db.coursesDao().insert(new Course(1, 2021, "FA", "CSE", "100"));
            //db.coursesDao().insert(new Course(1, 2021, "FA", "CSE", "110"));

//            db.coursesDao().insert(new Course(4, 2021, "FA", "CSE", "100"));
//            db.coursesDao().insert(new Course(4, 2020, "SP", "CSE", "101"));
//            db.coursesDao().insert(new Course(4, 2020, "SP", "PHIL", "27"));
//        }

        // END OF TESTING
        user = db.studentWithCoursesDao().getUser();

        if (user == null) {
            Student defaultUser = new Student(db.studentWithCoursesDao().count(), "Default User", "");
            defaultUser.isUser = true;
            db.studentWithCoursesDao().insert(defaultUser);
            user = db.studentWithCoursesDao().getUser();
        }

        Log.d(TAG, "User name: " + user.getName());
        Log.d(TAG, "User URL: " + user.getPhotoURL());
        for (String course : user.courses) {
            Log.d(TAG, "User course: " + course);
        }

        // May not be sorted yet, we do that later
        this.publishedMessage = new Message(user.toByteArray());

        prioritySpinner = findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());
        prioritySpinner.setSelection(0);

        matchedStudentsView = findViewById(R.id.matched_students_view);
        studentsLayoutManager = new LinearLayoutManager(getApplicationContext());
        matchedStudentsView.setLayoutManager(studentsLayoutManager);

        studentsViewAdapter = new StudentsViewAdapter(studentSorter.getSortedStudents(
                prioritySpinner.getSelectedItemPosition()));
        matchedStudentsView.setAdapter(studentsViewAdapter);
    }

    public void onAddCoursesClicked(View view) {
        Log.d(TAG, "Add courses button was clicked!");
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    public void onStartClicked(View view) {
        Log.d(TAG, "Start search button was clicked!");
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
                db.studentWithCoursesDao().insert(foundStudent.student);
                for(String courseTitle : foundStudent.courses) {
                    db.coursesDao().insert(new Course(courseTitle, foundStudent.getId()));
                }
                studentsViewAdapter.addStudent(studentSorter.getSortedStudents(
                        prioritySpinner.getSelectedItemPosition()), HomeActivity.this);
                Log.d(TAG, "New otherStudents size: " + studentSorter.getSortedStudents(
                        prioritySpinner.getSelectedItemPosition()));
            }
        };

        // Build a fake student
        StudentWithCourses fakedMessageStudent = new StudentWithCourses();
        fakedMessageStudent.student = new Student(0, "Jacob", "https://cdn.wccftech.com/wp-content/uploads/2017/07/nearby_connections.png");
        fakedMessageStudent.courses.add(new Course(0, 2021, "FA", "CSE", "110", 0)
                .courseTitle);

        //eventually not faked
        this.messageListener = new FakedMessageListener(realListener, 10, this);
        Nearby.getMessagesClient(this).subscribe(messageListener);
        Nearby.getMessagesClient(this).publish(publishedMessage);
    }

    public void onStopClicked(View view) {
        Log.d(TAG, "Stop button was clicked!");
        Button start = findViewById(R.id.start_btn);
        start.setVisibility(View.VISIBLE);

        Button stop = findViewById(R.id.stop_btn);
        stop.setVisibility(View.GONE);

        ((FakedMessageListener) messageListener).stopExecutor();
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        Nearby.getMessagesClient(this).unpublish(this.publishedMessage);
    }

    public void onMockNearbyMessagesClicked(View view) {
        Log.d(TAG, "Mock button was clicked!");
        Intent intent = new Intent(this, NearbyMessagesMockActivity.class);
        startActivity(intent);
    }

    public void onViewFavoritesClicked(View view) {
        Log.d(TAG, "View favorites button was clicked!");
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            studentsViewAdapter.addStudent(studentSorter.getSortedStudents(
                    prioritySpinner.getSelectedItemPosition()), HomeActivity.this);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            //do nothing, something should always be selected
            //must be implemented due to interface
        }
    }
}