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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent intent = getIntent();
        //int studentId = intent.getIntExtra("student_id", 0);

        db = AppDatabase.singleton(this);
    }

    public void onAddButtonClicked(View view) {
        addCourse();
    }

    public void onDoneButtonClicked(View view) {
        TextView yearTextView = findViewById(R.id.editYearTextView);
        TextView quarterTextView = findViewById(R.id.editQuarterTextView);
        TextView subjectTextView = findViewById(R.id.editSubjectTextView);
        TextView courseNumTextView = findViewById(R.id.editCourseNumTextView);

        if(!(yearTextView.getText().toString().equals("")
                && quarterTextView.getText().toString().equals("")
                && subjectTextView.getText().toString().equals("")
                && courseNumTextView.getText().toString().equals("")
        )) {
            addCourse();
        }

        finish();
    }

    private void addCourse() {
        TextView yearTextView = findViewById(R.id.editYearTextView);
        TextView quarterTextView = findViewById(R.id.editQuarterTextView);
        TextView subjectTextView = findViewById(R.id.editSubjectTextView);
        TextView courseNumTextView = findViewById(R.id.editCourseNumTextView);

        try {
            int year = Integer.parseInt(yearTextView.getText().toString());
            String quarter = quarterTextView.getText().toString();
            String subject = subjectTextView.getText().toString();
            String courseNum = courseNumTextView.getText().toString();

            Course newCourse = new Course(db.coursesDao().getCourses().size() + 1, year,
                    quarter, subject, courseNum);

            db.coursesDao().insert(newCourse);

            CourseUtilities.showAlert(this, "Course added!");

        } catch (Exception ex) {
            CourseUtilities.showError(this, "Something was incorrectly formatted!"
                    + "\n" + ex.toString());
        }
    }
}