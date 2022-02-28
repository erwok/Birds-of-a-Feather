package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;
import com.example.birdsofafeather.model.db.StudentWithCourses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearbyMessagesMockActivity extends AppCompatActivity {
    private static final String TAG = "Mock_Nearby_Messages";

    private static final String[] QUARTERS = new String[] {
            "FA", "WI", "SP", "SS1", "SS2", "SSS"
    };

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_messsages_mock);
    }

    public void onAddMockStudentClicked(View view) {
        Log.d(TAG, "Add mock student button was clicked!");

        TextView mockStudentInfoText = findViewById(R.id.paste_info_text);
        String[] mockStudentInfoTemp = mockStudentInfoText.getText().toString().split("\n");
        List<String> mockStudentInfo = new ArrayList<String>();
        for (int i = 0; i < mockStudentInfoTemp.length; i++) {
            if (!mockStudentInfoTemp[i].equals("")) {
                mockStudentInfo.add(mockStudentInfoTemp[i]);
            }
        }

        String mockStudentName = mockStudentInfo.get(0).replaceAll(",", "");
        String mockStudentGooglePhotoUrl = mockStudentInfo.get(1).replaceAll(",", "");
        int mockStudentId = AppDatabase.singleton(this).studentWithCoursesDao().count() + 1;

        Student mockStudent = new Student(mockStudentId, mockStudentName, mockStudentGooglePhotoUrl);
        mockStudent.sessionID = 0;
        AppDatabase.singleton(this).studentWithCoursesDao().insert(mockStudent);

        for (int i = 2; i < mockStudentInfo.size(); i++) {
            try {
                String[] courseInfo = mockStudentInfo.get(i).split(",");
                int year = Integer.parseInt(courseInfo[0]);
                String quarter = courseInfo[1];
                String subject = courseInfo[2];
                String courseNum = courseInfo[3];
                int size = Integer.parseInt(courseInfo[4]);

                if(!Arrays.asList(QUARTERS).contains(quarter)) {
                    throw new Exception();
                }

                Course newCourse = new Course(mockStudentId, year, quarter, subject, courseNum, size);

                AppDatabase.singleton(this).coursesDao().insert(newCourse);
                // We've added a new shared course, so invalidate all previous shared course counts.
                AppDatabase.singleton(this).studentWithCoursesDao().resetSharedCourses();

            } catch (Exception ex) {
                CourseUtilities.showError(this, "Something was incorrectly formatted!"
                        + "\n" + ex);
            }
        }

        // Reset the mock student info
        mockStudentInfoText.setText("");

        CourseUtilities.showAlert(this, "Mock Student added!");
    }

    public void onMockGoBackClicked(View view) {
        Log.d(TAG, "Mock go back to home screen button clicked");
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }
}