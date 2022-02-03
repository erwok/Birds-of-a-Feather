package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;

import java.util.List;

public class AddCourseActivity extends AppCompatActivity {
    private AppDatabase db;
    private IStudent student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent intent = getIntent();
        int studentId = intent.getIntExtra("student_id", 0);

        db = AppDatabase.singleton(this);
        List<Course> courses = db.coursesDao().getCourses();
    }

    public void onAddButtonClicked(View view) {
        //CourseUtilities.showAlert(this, "Got here");
        int newNoteId = db.coursesDao().count() + 1;

        TextView yearTextView = findViewById(R.id.editYearTextView);
        TextView quarterTextView = findViewById(R.id.editQuarterTextView);
        TextView subjectTextView = findViewById(R.id.editSubjectTextView);
        TextView courseNumTextView = findViewById(R.id.editCourseNumTextView);

        try {
            int year = Integer.parseInt(yearTextView.getText().toString());
            String quarter = quarterTextView.getText().toString();
            String subject = subjectTextView.getText().toString();
            int courseNum = Integer.parseInt(courseNumTextView.getText().toString());

            Course newCourse = new Course(year, quarter, subject, courseNum);
            db.coursesDao().insert(newCourse);

            CourseUtilities.showAlert(this, "Course added!");

        } catch (Exception ex) {
            CourseUtilities.showError(this, "Something was incorrectly formatted!");
        }
    }

    public void onDoneButtonClicked(View view) {
        finish();
    }
}