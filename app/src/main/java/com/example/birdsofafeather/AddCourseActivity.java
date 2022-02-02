package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
    }

    public void onAddButtonClicked(View view) {
        CourseUtilities.showAlert(this, "Does nothing!");
    }

    public void onDoneButtonClicked(View view) {
        finish();
    }
}