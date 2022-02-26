package com.example.birdsofafeather.model.db;

import java.util.List;

public class StudentSorter {
    private final int COMM_COURSE = 0;
    private final int THIS_Q = 1;
    private final int CLASS_SIZE = 2;
    private final int RECENCY = 3;
    private final int WAVED_AT = 4;

    private final int[] classSizeWeight = new int[] {100, 33, 18, 10, 6, 3};

    private AppDatabase db;

    public StudentSorter(AppDatabase db) {
        this.db = db;
    }

    public List<StudentWithCourses> getSortedStudents(int sortType) {
        switch(sortType) {
            case THIS_Q:
                return db.studentWithCoursesDao().getSortedOtherStudentsByThisQuarter();
            case CLASS_SIZE:
                List<StudentWithCourses> students = db.studentWithCoursesDao()
                        .getSortedOtherStudentsByThisQuarter();
                for(StudentWithCourses swc : students) {
                    int score = 0;
                    for(String course : swc.courses) {
                        score += classSizeWeight[db.coursesDao().getCourseSizeForCourse(
                                swc.getId(), course)];
                    }
                    swc.student.sizeScore = score;
                    db.studentWithCoursesDao().updateStudent(swc.student);
                }
                return db.studentWithCoursesDao().getSortedOtherStudentsByCourseSize();
            default:
                return db.studentWithCoursesDao().getSortedOtherStudents();
        }
    }
}
