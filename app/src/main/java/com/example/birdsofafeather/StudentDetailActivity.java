package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.StudentWithCourses;

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

        StudentWithCourses student = AppDatabase.singleton(this).studentWithCoursesDao().get(studentId);
        StudentWithCourses user = AppDatabase.singleton(this).studentWithCoursesDao().getUser();

        List<String> matchedCourses = student.overlappingClasses(user);

        TextView studentName = findViewById(R.id.student_name_textview);
        TextView studentMatched = findViewById(R.id.student_matched_textview);
        studentName.setText(student.getName());
        studentMatched.setText(getString(R.string.matched_courses, numCommonCourses));
        ImageView studentImage = findViewById(R.id.student_profile_imageview);
        Glide.with(this)
                .load(student.getPhotoURL())
                .fitCenter()
                .into(studentImage);

        RecyclerView courseRecyclerView = findViewById(R.id.matched_courses_recyclerview);
        RecyclerView.LayoutManager courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);

        CourseViewAdapter courseViewAdapter = new CourseViewAdapter(matchedCourses);
        courseRecyclerView.setAdapter(courseViewAdapter);
    }

    public void onCloseButtonClicked(View view) {
        finish();
    }

    public void onWaveToStudentClicked(View view) {
        ImageButton emptyWaveHand = (ImageButton) findViewById(R.id.empty_hand_wave_id2);
        emptyWaveHand.setVisibility(View.GONE);

        ImageButton filledWaveHand = (ImageButton) findViewById(R.id.filled_hand_wave_id2);
        filledWaveHand.setVisibility(View.VISIBLE);
    }

    public void onUnwaveClicked(View view) {
        ImageButton emptyWaveHand = (ImageButton) findViewById(R.id.empty_hand_wave_id2);
        emptyWaveHand.setVisibility(View.VISIBLE);

        ImageButton filledWaveHand = (ImageButton) findViewById(R.id.filled_hand_wave_id2);
        filledWaveHand.setVisibility(View.GONE);
    }
}