package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;

public class StudentDetailActivity extends AppCompatActivity {
    private final String MATCHED_COURSES = "Matched Courses: ";

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

        TextView studentName = findViewById(R.id.student_name_textview);
        TextView studentMatched = findViewById(R.id.student_matched_textview);
        studentName.setText(student.getName());
        studentMatched.setText(MATCHED_COURSES + commCourses);
        // ImageView studentImage = set for pfp

    }

    public void onCloseButtonClicked(View view) {
        finish();
    }
}