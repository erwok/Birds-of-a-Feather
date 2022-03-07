package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Student;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class MS2Story4Test {
    AppDatabase db;
    ActivityScenario<StudentDetailActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testWaveUI() {
        Student defaultUser = new Student(db.studentWithCoursesDao().count(), "Default User",
                "", UUID.randomUUID().toString());
        defaultUser.isUser = true;
        db.studentWithCoursesDao().insert(defaultUser);

        Student student1 = new Student(1, "Jeff", "google.jpg", UUID.randomUUID().toString());
        db.studentWithCoursesDao().insert(student1);
        Intent intent = new Intent();
        intent.putExtra(StudentsViewAdapter.STUDENT_ID_EXTRA, 1);

        scenario = ActivityScenario.launch(StudentDetailActivity.class);
        scenario.onActivity(activity ->{
            ImageButton emptyWaveHand = (ImageButton) activity.findViewById(R.id.empty_hand_wave_id2);
            ImageButton filledWaveHand = (ImageButton) activity.findViewById(R.id.filled_hand_wave_id2);

            emptyWaveHand.callOnClick();
            assertEquals(View.GONE, emptyWaveHand.getVisibility());
            assertEquals(View.VISIBLE, filledWaveHand.getVisibility());

            filledWaveHand.callOnClick();
            assertEquals(View.VISIBLE, emptyWaveHand.getVisibility());
            assertEquals(View.GONE, filledWaveHand.getVisibility());
        });
    }
}
