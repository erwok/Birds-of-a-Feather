package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.google.android.gms.nearby.messages.MessageListener;

public class FavoritesActivity extends AppCompatActivity {
    protected RecyclerView favoritesStudentView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentsViewAdapter studentsViewAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        db = AppDatabase.singleton(getApplicationContext());

        favoritesStudentView = findViewById(R.id.favorites_recyclerview);
        studentsLayoutManager = new LinearLayoutManager(getApplicationContext());
        favoritesStudentView.setLayoutManager(studentsLayoutManager);

        studentsViewAdapter = new StudentsViewAdapter(db.studentWithCoursesDao().getFavoritedStudents());
        favoritesStudentView.setAdapter(studentsViewAdapter);
    }

    public void onBackHomeClicked(View view) {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }
}