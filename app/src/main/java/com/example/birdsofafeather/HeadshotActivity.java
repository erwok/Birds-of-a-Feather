package com.example.birdsofafeather;

import static com.example.birdsofafeather.CourseUtilities.showError;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;

public class HeadshotActivity extends AppCompatActivity {

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headshot);

        Intent intent = getIntent();
        userName = intent.getStringExtra(NameActivity.NAME_EXTRA);
    }

    public void saveURL(View view) {
        TextView urlBox = findViewById(R.id.URLBox);

        if (urlBox.getText().toString().endsWith(".png") || urlBox.getText().toString().endsWith(".jpg")) {
            // This is where we actually create the user entry
            Student user = new Student(HomeActivity.USER_ID, userName, urlBox.getText().toString());
            user.isUser = true;

            // Wipe any previous data
            AppDatabase.singleton(getApplicationContext()).clearAllTables();
            AppDatabase.singleton(getApplicationContext()).studentWithCoursesDao().insert(user);

            Intent intent = new Intent(this, AddCourseActivity.class);
            startActivity(intent);
        }
        else {
            showError(this, "URL must be a .png or .jpg");
        }
    }
}