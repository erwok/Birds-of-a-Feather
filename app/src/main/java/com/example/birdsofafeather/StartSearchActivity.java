package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.bluetooth.*;

public class StartSearchActivity extends AppCompatActivity {
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_search);
    }

    public void onStartSearchClicked(View view) {

        Button start = findViewById(R.id.start_search_button);
        start.setVisibility(View.GONE);
        Button stop = findViewById(R.id.stop_search_button);
        stop.setVisibility(View.VISIBLE);

        this.future = backgroundThreadExecutor.submit(() -> {




            return null;
        });

    }

    public void onStopSearchClicked(View view) {
        Button stop = findViewById(R.id.stop_search_button);
        stop.setVisibility(View.GONE);
        Button start = findViewById(R.id.start_search_button);
        start.setVisibility(View.VISIBLE);

        this.future.cancel(true);
    }
}




