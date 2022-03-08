package com.example.birdsofafeather;

import static android.os.Looper.getMainLooper;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

@RunWith(AndroidJUnit4.class)

public class MS2Story5Test {
    AppDatabase db;
    ActivityScenario<NameSessionActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
        scenario = ActivityScenario.launch(NameSessionActivity.class);
    }

    @After
    public void tearDown() {
        db.close();
        scenario.close();
    }

    @Test
    public void testNoSavedSessionEntered () {
        scenario.onActivity(activity -> {
            TextView sessionName = activity.findViewById(R.id.sessionName);
            assertEquals("", sessionName.getText().toString());

            Button button = activity.findViewById(R.id.enterButton);
            assertFalse(button.isEnabled());
        });
    }

    @Test
    public void testSavedSessionEntered () {
        scenario.onActivity(activity -> {

            Session firstSession = new Session("ElizabethSession");
            db.sessionDao().insert(firstSession);

            Button enterButton = activity.findViewById(R.id.enterButton);
            enterButton.callOnClick();

            assertEquals("ElizabethSession", firstSession.getSessionName());
        });
    }

    @Test
    public void testEmptySavedSessionConstructor() {
        scenario.onActivity(activity -> {

            Session mySession = new Session();
            db.sessionDao().insert(mySession);

            Button enterButton = activity.findViewById(R.id.enterButton);
            enterButton.callOnClick();

            assertTrue(mySession.getSessionName().matches("\\d{1,2}\\/\\d{1,2}\\/\\d{2} \\d{1,2}:\\d{2}"));
        });
    }
}


