package com.example.birdsofafeather.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class, Course.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;
    private static Context context;

    public static AppDatabase singleton(Context cont) {
        if(singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(cont, AppDatabase.class, "students.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            context = cont;
        }

        return singletonInstance;
    }

    public static AppDatabase reset() {
        singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "students.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        return singletonInstance;
    }

    public abstract StudentWithCoursesDao studentWithCoursesDao();

    public abstract CoursesDao coursesDao();
}
