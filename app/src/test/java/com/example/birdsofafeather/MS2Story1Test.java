package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MS2Story1Test {
    AppDatabase db;
    ActivityScenario<AddCourseActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
        scenario = ActivityScenario.launch(AddCourseActivity.class);
    }

    @After
    public void tearDown() {
        db.close();
        scenario.close();
    }

    @Test
    public void testValidCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Spinner classSizeSpinner = activity.findViewById(R.id.class_size_spinner);
            Button button = activity.findViewById(R.id.add_button);

            String year = "2021";
            String qtr = "FA";
            String subj = "CSE";
            String course = "110";
            yearTextView.setText(year);
            quarterTextView.setText(qtr);
            subjectTextView.setText(subj);
            courseNumTextView.setText(course);
            classSizeSpinner.setSelection(0);
            button.performClick();

            assertEquals(year, yearTextView.getText().toString());
            assertEquals(qtr, quarterTextView.getText().toString());
            assertEquals(subj, subjectTextView.getText().toString());
            assertEquals("", courseNumTextView.getText().toString());
        });
    }
}
