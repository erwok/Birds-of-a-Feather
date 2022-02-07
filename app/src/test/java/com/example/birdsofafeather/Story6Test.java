package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Story6Test {
    AppDatabase db;
    ActivityScenario<AddCourseActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(AddCourseActivity.class);
        db = AppDatabase.singleton(getApplicationContext());
        db.clearAllTables();
    }

    @After
    public void tearDown() {
        db = AppDatabase.reset();
    }

    @Test
    public void testNoAddedCourses() {
        assertEquals(db.coursesDao().count(), 0);
    }

    @Test
    public void testValidCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button button = activity.findViewById(R.id.add_button);

            yearTextView.setText("2021");
            quarterTextView.setText("FA");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            button.performClick();

            yearTextView.setText("2020");
            quarterTextView.setText("SP");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("100");
            button.performClick();

            assertEquals(2, db.coursesDao().count());
        });
    }

    @Test
    public void testInvalidCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button button = activity.findViewById(R.id.add_button);

            yearTextView.setText("F");
            quarterTextView.setText("FA");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            button.performClick();

            assertEquals(0, db.coursesDao().count());
        });
    }

    @Test
    public void testDoneOnValidCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button button = activity.findViewById(R.id.done_button);

            yearTextView.setText("2021");
            quarterTextView.setText("FA");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            button.performClick();

            assertEquals(0, db.coursesDao().count());
        });
    }

    @Test
    public void testDoneOnInvalidCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button button = activity.findViewById(R.id.done_button);

            yearTextView.setText("F");
            quarterTextView.setText("FA");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            button.performClick();

            assertEquals(0, db.coursesDao().count());
        });
    }

    @Test
    public void testDoneOnEmptyCourse() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button button = activity.findViewById(R.id.done_button);

            yearTextView.setText("");
            quarterTextView.setText("");
            subjectTextView.setText("");
            courseNumTextView.setText("");
            button.performClick();

            assertEquals(0, db.coursesDao().count());
        });
    }

    @Test
    public void testAddAndDoneTogether() {
        scenario.onActivity(activity -> {
            TextView yearTextView = activity.findViewById(R.id.editYearTextView);
            TextView quarterTextView = activity.findViewById(R.id.editQuarterTextView);
            TextView subjectTextView = activity.findViewById(R.id.editSubjectTextView);
            TextView courseNumTextView = activity.findViewById(R.id.editCourseNumTextView);
            Button addButton = activity.findViewById(R.id.add_button);
            Button doneButton = activity.findViewById(R.id.add_button);

            yearTextView.setText("2021");
            quarterTextView.setText("FA");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            addButton.performClick();

            yearTextView.setText("2020");
            quarterTextView.setText("SP");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("100");
            addButton.performClick();

            yearTextView.setText("2019");
            quarterTextView.setText("Fall");
            subjectTextView.setText("CSE");
            courseNumTextView.setText("110");
            doneButton.performClick();

            assertEquals(2, db.coursesDao().count());
        });
    }
}
