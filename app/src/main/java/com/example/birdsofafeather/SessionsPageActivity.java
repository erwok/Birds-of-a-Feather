package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Session;
import com.example.birdsofafeather.model.db.StudentWithCourses;

import java.util.List;

public class SessionsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_page);

        List<Session> sessions = AppDatabase.singleton(this).sessionDao().getAll();

        RecyclerView sessionRecyclerView = findViewById(R.id.sessions_list);
        RecyclerView.LayoutManager sessionsLayoutManager = new LinearLayoutManager(this);
        sessionRecyclerView.setLayoutManager(sessionsLayoutManager);

        SessionViewAdapter sessionViewAdapter = new SessionViewAdapter(sessions);
        sessionRecyclerView.setAdapter(sessionViewAdapter);
    }

    public void onBackButtonClicked(View view) {
        finish();
    }
}