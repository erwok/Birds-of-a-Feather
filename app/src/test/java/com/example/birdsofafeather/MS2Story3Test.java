package com.example.birdsofafeather;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static org.junit.Assert.assertEquals;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.Course;
import com.example.birdsofafeather.model.db.Student;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MS2Story3Test {
    AppDatabase db;
    ActivityScenario<FavoritesActivity> scenario;

    @Before
    public void init() {
        AppDatabase.useTestSingleton(getApplicationContext());
        db = AppDatabase.singleton(getApplicationContext());
    }

    @After
    public void tearDown() {
        db.close();
    }

    @Test
    public void testRecyclerViewLength() {
        Student defaultUser = new Student(db.studentWithCoursesDao().count(), "Default User", "", UUID.randomUUID().toString());
        defaultUser.isUser = true;
        db.studentWithCoursesDao().insert(defaultUser);

        Student student1 = new Student(1, "Jeff", "google.jpg", UUID.randomUUID().toString());
        Student student2 = new Student(2, "Eric", "gmail.jpg", UUID.randomUUID().toString());

        db.studentWithCoursesDao().insert(student1);
        db.studentWithCoursesDao().insert(student2);
        db.studentWithCoursesDao().favoriteStudent(1, true);
        assertEquals(1, db.studentWithCoursesDao().getFavoritedStudents().size());

        scenario = ActivityScenario.launch(FavoritesActivity.class);
        scenario.onActivity(activity ->{
            RecyclerView favoritedStudents = activity.findViewById(R.id.favorites_recyclerview);
            assertEquals(1, favoritedStudents.getChildCount());
        });
    }


    @Test
    public void testFavoriteStudent() {
        Student student1 = new Student(1, "Jeff", "google.jpg", UUID.randomUUID().toString());
        Student student2 = new Student(2, "Eric", "gmail.jpg", UUID.randomUUID().toString());

        db.studentWithCoursesDao().insert(student1);
        db.studentWithCoursesDao().insert(student2);

        assertEquals(0, db.studentWithCoursesDao().getFavoritedStudents().size());

        db.studentWithCoursesDao().favoriteStudent(1, true);
        assertEquals(1, db.studentWithCoursesDao().getFavoritedStudents().size());

        db.studentWithCoursesDao().favoriteStudent(2, true);
        assertEquals(2, db.studentWithCoursesDao().getFavoritedStudents().size());
    }
}
