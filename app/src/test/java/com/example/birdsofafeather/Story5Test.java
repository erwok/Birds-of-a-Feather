package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;

@RunWith(AndroidJUnit4.class)
public class Story5Test {

    ActivityScenario<HeadshotActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(HeadshotActivity.class);
    }

    @Test
    public void testPNGFile() {
        scenario.onActivity(activity ->{

            TextView url = activity.findViewById(R.id.URLBox);
            url.setText("birdsOfAFeather.png");

            Button button = activity.findViewById(R.id.submitButton);
            button.callOnClick();

            Intent expectedIntent = new Intent(activity, AddCourseActivity.class);
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();
            assertTrue(actualIntent.filterEquals(expectedIntent));
        });

    }

    @Test
    public void testJPGFile() {
        scenario.onActivity(activity ->{

            TextView url = activity.findViewById(R.id.URLBox);
            url.setText("birdsOfAFeather.jpg");

            Button button = activity.findViewById(R.id.submitButton);
            button.callOnClick();

            Intent expectedIntent = new Intent(activity, AddCourseActivity.class);
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();
            assertTrue(actualIntent.filterEquals(expectedIntent));
        });

    }

    @Test
    public void testNeitherFiles() {
        scenario.onActivity(activity ->{

            TextView url = activity.findViewById(R.id.URLBox);
            url.setText("birdsOfAFeather.pdf");

            Button button = activity.findViewById(R.id.submitButton);
            button.callOnClick();

            final AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
            final ShadowAlertDialog shadowAlertDialog = Shadows.shadowOf(dialog);
            assertEquals(shadowAlertDialog.getTitle(), "Error!");
            assertEquals(shadowAlertDialog.getMessage(), "URL must be a .png or .jpg");
        });

    }

}
