package com.example.birdsofafeather.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class, Course.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if(singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "students.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return singletonInstance;
    }

    public static void useTestSingleton(Context context) {
        singletonInstance = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract StudentWithCoursesDao studentWithCoursesDao();

    public abstract CoursesDao coursesDao();
}
