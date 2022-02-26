package com.example.birdsofafeather.model.db;

import java.util.List;

public class StudentSorter {
    private final int COMM_COURSE = 0;
    private final int THIS_Q = 1;
    private final int CLASS_SIZE = 2;
    private final int RECENCY = 3;
    private final int WAVED_AT = 4;

    private AppDatabase db;

    public StudentSorter(AppDatabase db) {
        this.db = db;
    }

    public List<StudentWithCourses> getSortedStudents(int sortType) {
        switch(sortType) {
            case THIS_Q:
                return db.studentWithCoursesDao().getSortedOtherStudentsByThisQuarter();
            default:
                return db.studentWithCoursesDao().getSortedOtherStudents();
        }
    }
}
