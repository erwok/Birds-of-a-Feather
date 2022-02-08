package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailActivity extends AppCompatActivity {
    private final String MATCHED_COURSES = "Matched Courses: ";
    private static final int USER_ID = 0;

    private AppDatabase db;
    private IStudent student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Intent intent = getIntent();
        int studentId = intent.getIntExtra("student_id", 0);
        int commCourses = intent.getIntExtra("comm_courses", 0);

        db = AppDatabase.singleton(this);
        student = db.studentWithCoursesDao().get(studentId);
        List<String> studentCourses = student.getClasses();
        IStudent user = db.studentWithCoursesDao().get(USER_ID);
        List<String> userCourses = user.getClasses();

        List<String> matchedCourses = new ArrayList<>();
        for(String uc : userCourses) {
            if(studentCourses.contains(uc)) {
                matchedCourses.add(uc);
            }
        }

        TextView studentName = findViewById(R.id.student_name_textview);
        TextView studentMatched = findViewById(R.id.student_matched_textview);
        studentName.setText(student.getName());
        studentMatched.setText(MATCHED_COURSES + commCourses);
        // ImageView studentImage = set for pfp

        RecyclerView courseRecyclerView = findViewById(R.id.matched_courses_recyclerview);
        RecyclerView.LayoutManager courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);

        CourseViewAdapter courseViewAdapter = new CourseViewAdapter(matchedCourses);
        courseRecyclerView.setAdapter(courseViewAdapter);
    }

    public void onCloseButtonClicked(View view) {
        finish();
    }
}