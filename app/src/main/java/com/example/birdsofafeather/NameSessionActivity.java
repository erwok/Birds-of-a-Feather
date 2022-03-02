package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Session;

public class NameSessionActivity extends AppCompatActivity {

    public static final String SESSION_ID_EXTRA = "session_id";

    private int sessionID;
    TextView sessionNameText;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_session);
        Intent intent = getIntent();
        sessionID = intent.getIntExtra(SESSION_ID_EXTRA, -1);

        enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(this::onEnterClicked);
        sessionNameText = findViewById(R.id.sessionName);


        sessionNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enterButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onEnterClicked(View view) {
        AppDatabase db = AppDatabase.singleton(this);
        db.sessionDao().renameSession(sessionID, sessionNameText.getText().toString());
        finish();
    }
}