package com.example.birdsofafeather;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

@RunWith(AndroidJUnit4.class)
public class Story4Test {

    ActivityScenario<NameActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        scenario = ActivityScenario.launch(NameActivity.class);
    }

    @Test
    public void testLaunchesHeadshot() {
        scenario.onActivity(activity ->{
            TextView editTextName = activity.findViewById(R.id.editTextName);
            editTextName.setText("Elizabeth");

            Button button = activity.findViewById(R.id.confirmButton);
            button.callOnClick();

            Intent expectedIntent = new Intent(activity, HeadshotActivity.class);
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();
            assertTrue(actualIntent.filterEquals(expectedIntent));
        });
    }

    @Test
    public void testNoName() {
        scenario.onActivity(activity -> {
            TextView editTextName = activity.findViewById(R.id.editTextName);
            assertEquals("", editTextName.getText().toString());

            Button button = activity.findViewById(R.id.confirmButton);
            assertFalse(button.isEnabled());
        });
    }

    @Test
    public void testValidName() {
        scenario.onActivity(activity -> {
            TextView editTextName = activity.findViewById(R.id.editTextName);
            editTextName.setText("Rye");

            Button button = activity.findViewById(R.id.confirmButton);
            assertTrue(button.isEnabled());

            button.callOnClick();

            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();
            assertEquals("Rye", actualIntent.getStringExtra(NameActivity.NAME_EXTRA));
        });
    }


}
