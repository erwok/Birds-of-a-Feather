package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class headshot extends AppCompatActivity {

    public static final String URL_PREFERENCE_KEY = "url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headshot);

    }

    public void saveURL(View view) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        TextView urlBox = findViewById(R.id.URLBox);
        editor.putString(URL_PREFERENCE_KEY, urlBox.getText().toString());

        editor.apply();

        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);


    }
}