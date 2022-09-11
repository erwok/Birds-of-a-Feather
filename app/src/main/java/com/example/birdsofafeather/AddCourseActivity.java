package com.example.birdsofafeather;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;

import java.util.Arrays;

/**
 * Activity page handling student users adding courses they've taken
 * to their profile.
 */
public class AddCourseActivity extends AppCompatActivity {
    protected Spinner classSizeSpinner;
    private static final String[] QUARTERS = new String[] {
            "FA", "WI", "SP", "SS1", "SS2", "SSS"
    };

    /**
     * UI set up upon page being accessed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, QUARTERS);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editQuarterTextView);
        textView.setAdapter(adapter);

        classSizeSpinner = findViewById(R.id.class_size_spinner);
        ArrayAdapter<CharSequence> classSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.class_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSizeSpinner.setAdapter(classSizeAdapter);
        classSizeSpinner.setSelection(5);
    }

    /**
     * Add button onClick listener.
     *
     * @param view
     */
    public void onAddButtonClicked(View view) {
        addCourse();
    }

    /**
     * Done button onClick listener.
     *
     * @param view
     */
    public void onDoneButtonClicked(View view) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }

    /**
     * Helper method validating user input course information and updating
     * frontend displayed courses.
     */
    private void addCourse() {
        // Acquire course year, quarter, subject, and course number
        TextView yearTextView = findViewById(R.id.editYearTextView);
        TextView quarterTextView = findViewById(R.id.editQuarterTextView);
        TextView subjectTextView = findViewById(R.id.editSubjectTextView);
        TextView courseNumTextView = findViewById(R.id.editCourseNumTextView);

        try {
            int year = Integer.parseInt(yearTextView.getText().toString());
            String quarter = quarterTextView.getText().toString();
            String subject = subjectTextView.getText().toString();
            String courseNum = courseNumTextView.getText().toString();

            // Validate course information
            if(!Arrays.asList(QUARTERS).contains(quarter)) {
                throw new Exception();
            }

            if (subject.equals("")) {
                throw new Exception();
            }

            if (courseNum.equals("")) {
                throw new Exception();
            }

            Course newCourse = new Course(HomeActivity.USER_ID, year, quarter, subject, courseNum,
                    classSizeSpinner.getSelectedItemPosition());

            // Insert valid course into database
            AppDatabase.singleton(this).coursesDao().insert(newCourse);
            // Previous shared course counts invalidated after shared course insertion
            AppDatabase.singleton(this).studentWithCoursesDao().resetSharedCourses();

            // Reset the course number
            courseNumTextView.setText("");

            CourseUtilities.showAlert(this, "Course added!");

        } catch (Exception ex) {
            CourseUtilities.showError(this, "Something was incorrectly formatted!"
                    + "\n" + ex);
        }
    }
}