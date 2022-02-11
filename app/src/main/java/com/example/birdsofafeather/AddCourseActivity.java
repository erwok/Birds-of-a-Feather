package com.example.birdsofafeather;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;

import java.util.Arrays;

public class AddCourseActivity extends AppCompatActivity {

    private static final String[] QUARTERS = new String[] {
            "FA", "WI", "SP", "SS1", "SS2", "SSS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, QUARTERS);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editQuarterTextView);
        textView.setAdapter(adapter);
    }

    public void onAddButtonClicked(View view) {
        addCourse();
    }

    public void onDoneButtonClicked(View view) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
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
            int courseNum = Integer.parseInt(courseNumTextView.getText().toString());

            if(!Arrays.asList(QUARTERS).contains(quarter)) {
                throw new Exception();
            }

            Course newCourse = new Course(HomeActivity.USER_ID, year, quarter, subject, courseNum);

            AppDatabase.singleton(this).coursesDao().insert(newCourse);
            // We've added a new shared course, so invalidate all previous shared course counts.
            AppDatabase.singleton(this).studentWithCoursesDao().resetSharedCourses();

            CourseUtilities.showAlert(this, "Course added!");

        } catch (Exception ex) {
            CourseUtilities.showError(this, "Something was incorrectly formatted!"
                    + "\n" + ex);
        }
    }
}