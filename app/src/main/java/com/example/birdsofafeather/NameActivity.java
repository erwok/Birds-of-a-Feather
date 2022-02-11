package com.example.birdsofafeather;


import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

public class NameActivity extends AppCompatActivity {

    public static final String NAME_EXTRA = "name";

    private static final int REQ_ONE_TAP = 2;
    private static final String TAG = "BoaF_NameActivity";

    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    private EditText editTextName;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        // For debugging
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.remove(NAME_PREFERENCE_KEY);
//        editor.commit();

        editTextName = findViewById(R.id.editTextName);
        confirmButton = findViewById(R.id.confirmButton);
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_ID))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

    }

    public void saveName(View view) {
        TextView nameView = findViewById(R.id.editTextName);

        Intent intent = new Intent(this, HeadshotActivity.class);
        // We don't create a DB entry for the user here - we just pass their name to the headshot
        // activity and do it there.
        intent.putExtra(NAME_EXTRA, nameView.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AppDatabase.singleton(getApplicationContext()).studentWithCoursesDao().getUser() == null) {
            oneTapClient.beginSignIn(signUpRequest)
                    .addOnSuccessListener(this, result -> {
                        try {
                            //noinspection deprecation
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d(TAG, "No Google Accounts found. Just continue presenting the signed-out UI. Error: " + e.getLocalizedMessage());
                    });
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    editTextName.setText(credential.getGivenName());
                    confirmButton.setEnabled(true);
                    Log.d(TAG, "Got ID token.");
                }
            } catch (ApiException e) {
                Log.e(TAG, "Error getting ID token. Error: " + e.getLocalizedMessage());
            }
        }
    }

}