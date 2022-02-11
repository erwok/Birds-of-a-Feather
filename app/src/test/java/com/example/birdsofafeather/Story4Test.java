package com.example.birdsofafeather;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
            SharedPreferences shared = activity.getPreferences(MODE_PRIVATE);
            String var = shared.getString(NameActivity.NAME_PREFERENCE_KEY,"string not in shared preferences");
            assertEquals(var, "Rye");
        });
    }


}
