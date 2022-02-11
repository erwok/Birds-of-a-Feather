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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Intent intent = getIntent();
        int studentId = intent.getIntExtra(StudentsViewAdapter.STUDENT_ID_EXTRA, 0);
        int numCommonCourses = intent.getIntExtra(StudentsViewAdapter.COMMON_COURSES_EXTRA, 0);

        IStudent student = AppDatabase.singleton(this).studentWithCoursesDao().get(studentId);
        List<String> studentCourses = student.getClasses();
        IStudent user = AppDatabase.singleton(this).studentWithCoursesDao().get(HomeActivity.USER_ID);
        List<String> userCourses = user.getClasses();

        List<String> matchedCourses = new ArrayList<>();
        for(String userCourse : userCourses) {
            if(studentCourses.contains(userCourse)) {
                matchedCourses.add(userCourse);
            }
        }

        TextView studentName = findViewById(R.id.student_name_textview);
        TextView studentMatched = findViewById(R.id.student_matched_textview);
        studentName.setText(student.getName());
        studentMatched.setText(getString(R.string.matched_courses, numCommonCourses));
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