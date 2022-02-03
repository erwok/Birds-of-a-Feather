package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.birdsofafeather.model.DummyStudent;
import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;

public class HomeActivity extends AppCompatActivity {
    protected IStudent student = new DummyStudent(0, "Daniel", "",
            new String[] {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
    }

    public void onAddCoursesClicked(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
}