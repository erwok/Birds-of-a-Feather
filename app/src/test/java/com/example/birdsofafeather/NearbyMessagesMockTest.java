package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Session;

@RunWith(AndroidJUnit4.class)
public class NearbyMessagesMockTest {
    AppDatabase db;
    ActivityScenario<NearbyMessagesMockActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
        scenario = ActivityScenario.launch(NearbyMessagesMockActivity.class);
    }

    @After
    public void tearDown() {
        db.close();
        scenario.close();
    }

    @Test
    public void testAddMockStudentToDatabase() {
        scenario.onActivity(activity -> {
            TextView pastedInfo = activity.findViewById(R.id.paste_info_text);
            pastedInfo.setText("Bill,,,\nhttps://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOS" +
                    "MOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76" +
                    "pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,," +
                    "\n2021,FA,CSE,210,0\n2022,WI,CSE,110,0\n2022,SP,CSE,110,0");

            Button enterMockStudentInfo = activity.findViewById(R.id.add_mock_student);
            db.sessionDao().insert(new Session());
            enterMockStudentInfo.callOnClick();

            // There should be 1 student after adding Bill
            assertEquals(1, db.studentWithCoursesDao().count());

            // Bill had 3 classes added to the database, so we're expecting 3 in total
            assertEquals(3, db.coursesDao().count());
        });
    }
}
