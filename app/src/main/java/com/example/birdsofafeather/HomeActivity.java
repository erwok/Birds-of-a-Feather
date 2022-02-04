package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.birdsofafeather.model.DummyStudent;
import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    protected RecyclerView matchedStudentsView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentsViewAdapter studentsViewAdapter;

    protected IStudent user = new DummyStudent(0, "Daniel", "",
            new String[] {});
    protected IStudent[] data = {
            new DummyStudent(0, "Elizabeth", "", new String[]{
                    "NOTHING YET"
            }),
            new DummyStudent(1, "Rye", "", new String[]{}),
            new DummyStudent(2, "Jeff", "", new String[]{}),
            new DummyStudent(3, "Helen", "", new String[]{}),
            new DummyStudent(4, "Eric", "", new String[]{})
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        //List<? extends IStudent> students = db.studentWithCoursesDao().getAll();
        List<? extends IStudent> students = Arrays.asList(data);

        matchedStudentsView = findViewById(R.id.matched_students_view);

        studentsLayoutManager = new LinearLayoutManager(this);
        matchedStudentsView.setLayoutManager(studentsLayoutManager);

        studentsViewAdapter = new StudentsViewAdapter(students);
        matchedStudentsView.setAdapter(studentsViewAdapter);
    }

    public void onAddCoursesClicked(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
}