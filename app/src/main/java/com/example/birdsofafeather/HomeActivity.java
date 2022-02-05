package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.birdsofafeather.model.DummyStudent;
import com.example.birdsofafeather.model.IStudent;
import com.example.birdsofafeather.model.db.AppDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {
    protected RecyclerView matchedStudentsView;
    protected RecyclerView.LayoutManager studentsLayoutManager;
    protected StudentsViewAdapter studentsViewAdapter;

    protected IStudent user = new DummyStudent(0, "Daniel", "",
            new String[] {
                    "DUMMY COURSE 1"
            });
    protected IStudent[] data = {
            new DummyStudent(0, "Elizabeth", "", new String[]{
                    "DUMMY COURSE 1"
            }),
            new DummyStudent(1, "Rye", "", new String[]{}),
            new DummyStudent(2, "Jeff", "", new String[]{}),
            new DummyStudent(3, "Helen", "", new String[]{
                    "DUMMY COURSE 1"
            }),
            new DummyStudent(4, "Eric", "", new String[]{})
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        //List<? extends IStudent> students = db.studentWithCoursesDao().getAll();
        List<IStudent> students = Arrays.asList(data);

        matchedStudentsView = findViewById(R.id.matched_students_view);

        studentsLayoutManager = new LinearLayoutManager(this);
        matchedStudentsView.setLayoutManager(studentsLayoutManager);

        Pair<List<IStudent>, List<Integer>> orderedStudents = orderStudents(students);
        studentsViewAdapter = new StudentsViewAdapter(orderedStudents.first
                , orderedStudents.second);
        matchedStudentsView.setAdapter(studentsViewAdapter);
    }

    private Pair<List<IStudent>, List<Integer>> orderStudents(List<? extends IStudent> currOrder) {
        List<Integer> commCourses = new ArrayList<>();
        List<IStudent> newOrder = new ArrayList<>();

        PriorityQueue<StudentByCommonCourses> pq = new PriorityQueue<>();

        for(IStudent curr: currOrder) {
            int cc = 0;
            List<String> courses = curr.getClasses();
            for(String c : courses) {
                if(user.getClasses().contains(c)){
                    cc++;
                }
            }

            pq.add(new StudentByCommonCourses(curr, cc));
        }

        while(!pq.isEmpty()) {
            StudentByCommonCourses sbcc = pq.poll();
            newOrder.add(sbcc.student);
            commCourses.add(sbcc.commCourse);
        }

        return new Pair<>(newOrder, commCourses);
    }

    public void onAddCoursesClicked(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    protected class StudentByCommonCourses implements Comparable {
        protected IStudent student;
        protected int commCourse;

        protected StudentByCommonCourses (IStudent student, int cc) {
            this.student = student;
            this.commCourse = cc;
        }

        @Override
        public int compareTo(Object o) {
            if(o instanceof StudentByCommonCourses) {
                return ((StudentByCommonCourses)o).commCourse - this.commCourse;
            } else {
                return 0;
            }
        }
    }
}