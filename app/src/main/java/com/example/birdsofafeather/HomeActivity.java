package com.example.birdsofafeather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Session;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentSorter;
import com.example.birdsofafeather.model.db.StudentWithCourses;
import com.example.birdsofafeather.model.db.Wave;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.List;
import java.util.UUID;

/**
 * Home page with functionality for various button clicks, UI displaying
 * user specific data, and setting up different session uses of the current user.
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "BoaF_Home";
    public static final int USER_ID = 0;
    public static final String HOME_SESSION_ID_EXTRA = "HOME_SESSION_ID";

    private Message publishedMessage;
    protected RecyclerView matchedStudentsView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentsViewAdapter studentsViewAdapter;
    private MessageListener messageListener;
    private AppDatabase db;

    protected Spinner prioritySpinner;
    public StudentSorter studentSorter;

    protected StudentWithCourses user;

    protected Session activeSession;

    /**
     * Acquire suer data from database and set up session data as well as
     * setting up the many layout components of the page with the user specific
     * data.
     *
     * @param savedInstanceState
     */
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = AppDatabase.singleton(getApplicationContext());
        studentSorter = new StudentSorter(db);

        user = db.studentWithCoursesDao().getUser();
        Intent intent = getIntent();

        // Sessions save previous searches of classmates and their data without
        // user favoriting or saving anything
        int sessionID = intent.getIntExtra(HOME_SESSION_ID_EXTRA, -1);
        if (sessionID == -1) {
            activeSession = db.sessionDao().getLast();
        } else {
            activeSession = db.sessionDao().get(sessionID);
        }
        if (activeSession == null) {
            activeSession = new Session();
        }

        if (user == null) {
            Student defaultUser = new Student(db.studentWithCoursesDao().count(), "Default User", "", UUID.randomUUID().toString());
            defaultUser.isUser = true;
            db.studentWithCoursesDao().insert(defaultUser);
            user = db.studentWithCoursesDao().getUser();
        }

        Log.d(TAG, "User name: " + user.getName());
        Log.d(TAG, "User URL: " + user.getPhotoURL());
        Log.d(TAG, "User UUID: " + user.getUUID());
        for (String course : user.courses) {
            Log.d(TAG, "User course: " + course);
        }

        this.publishedMessage = new Message(user.toByteArray());

        /**
         * Following code sets up UI components on home page.
         */
        prioritySpinner = findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        matchedStudentsView = findViewById(R.id.matched_students_view);
        studentsLayoutManager = new LinearLayoutManager(getApplicationContext());
        matchedStudentsView.setLayoutManager(studentsLayoutManager);

        studentsViewAdapter = new StudentsViewAdapter(studentSorter.getSortedStudents(
                prioritySpinner.getSelectedItemPosition(), activeSession.sessionID));
        matchedStudentsView.setAdapter(studentsViewAdapter);

        prioritySpinner.setOnItemSelectedListener(new SpinnerActivity());
        prioritySpinner.setSelection(0);

        CheckBox mockedCheckBox = findViewById(R.id.mock_checkbox);
        mockedCheckBox.setChecked(true);
    }

    /**
     * Add course button onClick listener.
     *
     * @param view
     */
    public void onAddCoursesClicked(View view) {
        Log.d(TAG, "Add courses button was clicked!");
        // Opens add course activity where users input required fields for a new course
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    /**
     * Start Bluetooth search of classmates with shared courses.
     * Nearby Messages API used to acquire data sent from other users
     * and to send the current user's data out for others to acquire.
     *
     * @param view
     */
    public void onStartClicked(View view) {
        Log.d(TAG, "Start search button was clicked!");
        Button start = findViewById(R.id.start_btn);
        start.setVisibility(View.GONE);

        Button stop = findViewById(R.id.stop_btn);
        stop.setVisibility(View.VISIBLE);

        // Create active session
        db.sessionDao().insert(new Session());
        activeSession = db.sessionDao().getLast();

        MessageListener realListener = new MessageListener() {
            //put information into database
            @Override
            public void onFound(@NonNull Message message) {
                Log.d(TAG, "Found message!");
                // Make sure we received a valid message.
                try {
                    StudentWithCourses foundStudent = new StudentWithCourses(
                            db.studentWithCoursesDao().count() + 1, message.getContent());
                    foundStudent.student.sessionID = activeSession.sessionID;

                    db.studentWithCoursesDao().insert(foundStudent.student);

                    for(String courseTitle : foundStudent.courses) {
                        db.coursesDao().insert(new Course(courseTitle, foundStudent.getId()));
                    }

                    Log.d(TAG, "New otherStudents size: " + studentSorter.getSortedStudents(
                            prioritySpinner.getSelectedItemPosition(), activeSession.sessionID));
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Message received not a student: " + e.getLocalizedMessage());
                    try {
                        Wave wave = new Wave(message.getContent());
                        StudentWithCourses student = db.studentWithCoursesDao().getWithUUID(wave.uuid);
                        if(student == null) {
                            return;
                        }
                        student.student.waveToMe = wave.waveAt;
                        db.studentWithCoursesDao().updateStudent(student.student);
                    } catch (IllegalArgumentException ex) {
                        Log.e(TAG, "Message received not a wave: " + ex.getLocalizedMessage());
                        return;
                    }
                    return;
                }
                studentsViewAdapter.addStudent(studentSorter.getSortedStudents(
                        prioritySpinner.getSelectedItemPosition(), activeSession.sessionID), HomeActivity.this);
            }
        };

        CheckBox mockedCheckBox = findViewById(R.id.mock_checkbox);
        if(mockedCheckBox.isChecked()) {
            this.messageListener = new FakedMessageListener(realListener, 5, this);
        }
        else {
            this.messageListener = realListener;
        }
        Nearby.getMessagesClient(this).subscribe(messageListener);
        Nearby.getMessagesClient(this).publish(publishedMessage);
    }

    /**
     * Halts a current search and stops user from sending and receiving data.
     *
     * @param view
     */
    public void onStopClicked(View view) {
        Log.d(TAG, "Stop button was clicked!");
        Button start = findViewById(R.id.start_btn);
        start.setVisibility(View.VISIBLE);

        Button stop = findViewById(R.id.stop_btn);
        stop.setVisibility(View.GONE);

        CheckBox mockedCheckBox = findViewById(R.id.mock_checkbox);
        if(mockedCheckBox.isChecked()) {
            ((FakedMessageListener) messageListener).stopExecutor();
        }
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
        Nearby.getMessagesClient(this).unpublish(this.publishedMessage);
    }

    /**
     * Mock nearby messages onClick listener.
     *
     * @param view
     */
    public void onMockNearbyMessagesClicked(View view) {
        Log.d(TAG, "Mock button was clicked!");
        Intent intent = new Intent(this, NearbyMessagesMockActivity.class);
        startActivity(intent);
    }

    /**
     * View favorites button onClick listener.
     * Opens up favorites page.
     *
     * @param view
     */
    public void onViewFavoritesClicked(View view) {
        Log.d(TAG, "View favorites button was clicked!");
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    /**
     * Save session button onClick listener.
     * Saves a current search session with found users and their data.
     *
     * @param view
     */
    public void onSaveSessionClicked(View view) {
        Log.d(TAG, "Save session button was clicked!");
        Intent intent = new Intent(this, NameSessionActivity.class);
        intent.putExtra(NameSessionActivity.SESSION_ID_EXTRA, activeSession.sessionID);
        startActivity(intent);
    }

    /**
     * View sessions button onClick listener.
     * Allows user to view old search sessions where they can view past matched students.
     *
     * @param view
     */
    public void onSessionsClicked(View view) {
        Log.d(TAG, "Sessions button clicked!");
        Intent intent = new Intent(this, SessionsPageActivity.class);
        startActivity(intent);
    }

    /**
     * UI activity designed to dynamically sort users upon new students being added to
     * users found in current session.
     */
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            studentsViewAdapter.addStudent(studentSorter.getSortedStudents(
                    prioritySpinner.getSelectedItemPosition(), activeSession.sessionID), HomeActivity.this);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            //do nothing, something should always be selected
            //must be implemented due to interface
        }
    }
}