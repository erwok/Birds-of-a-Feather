package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.example.birdsofafeather.model.db.Wave;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to hold found student's matched courses andwave status.
 */
public class StudentDetailActivity extends AppCompatActivity {
    private AppDatabase db;
    private Message publishedMessage;
    private final String WAVE_SENT = "Wave sent!";
    private final String WAVE_REMOVED = "Wave removed!";

    /**
     * UI initialization of a page showing a found student and their matched
     * courses with the current user.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        db = AppDatabase.singleton(getApplicationContext());

        Intent intent = getIntent();
        int studentId = intent.getIntExtra(StudentsViewAdapter.STUDENT_ID_EXTRA, 0);
        int numCommonCourses = intent.getIntExtra(StudentsViewAdapter.COMMON_COURSES_EXTRA, 0);

        StudentWithCourses student = AppDatabase.singleton(this).studentWithCoursesDao().get(studentId);
        StudentWithCourses user = AppDatabase.singleton(this).studentWithCoursesDao().getUser();

        List<String> matchedCourses = student.overlappingClasses(user);

        TextView studentName = findViewById(R.id.student_name_textview);
        TextView studentMatched = findViewById(R.id.student_matched_textview);
        studentName.setText(student.getName());
        studentMatched.setText(getString(R.string.matched_courses, numCommonCourses));
        ImageView studentImage = findViewById(R.id.student_profile_imageview);
        Glide.with(this)
                .load(student.getPhotoURL())
                .fitCenter()
                .into(studentImage);

        RecyclerView courseRecyclerView = findViewById(R.id.matched_courses_recyclerview);
        RecyclerView.LayoutManager courseLayoutManager = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(courseLayoutManager);

        CourseViewAdapter courseViewAdapter = new CourseViewAdapter(matchedCourses);
        courseRecyclerView.setAdapter(courseViewAdapter);
    }

    /**
     * Close button onClick listener.
     *
     * @param view
     */
    public void onCloseButtonClicked(View view) {
        finish();
    }

    /**
     * Send wave button onClick listener.
     * Sends wave to student who user clicks the hand wave button for.
     *
     * @param view
     */
    public void onWaveToStudentClicked(View view) {
        ImageButton emptyWaveHand = (ImageButton) findViewById(R.id.empty_hand_wave_id2);
        emptyWaveHand.setVisibility(View.GONE);

        ImageButton filledWaveHand = (ImageButton) findViewById(R.id.filled_hand_wave_id2);
        filledWaveHand.setVisibility(View.VISIBLE);

        Toast.makeText(this, WAVE_SENT, Toast.LENGTH_SHORT).show();

        Wave waveAt = new Wave(db.studentWithCoursesDao().getUser().getUUID(), true);
        publishedMessage = new Message(waveAt.toByteArray());
        Nearby.getMessagesClient(this).publish(publishedMessage);
    }

    /**
     * Unwave button onClick listener.
     * Unsends wave to selected student.
     *
     * @param view
     */
    public void onUnwaveClicked(View view) {
        ImageButton emptyWaveHand = (ImageButton) findViewById(R.id.empty_hand_wave_id2);
        emptyWaveHand.setVisibility(View.VISIBLE);

        ImageButton filledWaveHand = (ImageButton) findViewById(R.id.filled_hand_wave_id2);
        filledWaveHand.setVisibility(View.GONE);

        Wave waveAt = new Wave(db.studentWithCoursesDao().getUser().getUUID(), false);
        publishedMessage = new Message(waveAt.toByteArray());
        Toast.makeText(this, WAVE_REMOVED, Toast.LENGTH_SHORT).show();
    }
}