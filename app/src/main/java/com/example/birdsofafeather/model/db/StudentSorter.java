package com.example.birdsofafeather.model.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentSorter {
    private final int COMM_COURSE = 0;
    private final int THIS_Q = 1;
    private final int CLASS_SIZE = 2;
    private final int RECENCY = 3;
    private final int WAVE_FROM = 4;

    private final int[] classSizeWeight = new int[] {100, 33, 18, 10, 6, 3};
    private final List<String> quarters = new ArrayList<>(Arrays.asList("FA", null, "SP", "WI"));

    private AppDatabase db;

    public StudentSorter(AppDatabase db) {
        this.db = db;
    }

    public int calculateSizeScore(StudentWithCourses swc) {
        int sizeScore = 0;
        for(String course : swc.overlappingClasses(db.studentWithCoursesDao().getUser())) {
            sizeScore += classSizeWeight[db.coursesDao().getCourseSizeForCourse(
                    swc.getId(), course)];
        }
        return sizeScore;
    }

    public int calculateRecencyScore(StudentWithCourses swc) {
        int recencyScore = 0;
        for(String course : swc.overlappingClasses(db.studentWithCoursesDao().getUser())) {
            String[] courseInfo = course.split(" ");
            int year = Integer.parseInt(courseInfo[0]);
            String quarter = courseInfo[1];
            if(year == 2022) {
                recencyScore += 5;
            } else if(year == 2021) {
                int toSub = quarters.indexOf(quarter);
                if(toSub == -1) {
                    toSub = 1;
                }
                recencyScore += 5 - toSub;
            } else {
                recencyScore += 1;
            }
        }
        return recencyScore;
    }

    public int calculateSharedCourseCount(StudentWithCourses swc) {
        return swc.overlappingClasses(db.studentWithCoursesDao().getUser()).size();
    }

    public int calculateThisQuarterScore(StudentWithCourses swc) {
        int thisQScore = 0;
        for(String course : swc.overlappingClasses(db.studentWithCoursesDao().getUser())) {
            if(course.startsWith("2022 WI")) {
                thisQScore++;
            }
        }
        return thisQScore;
    }

    public List<StudentWithCourses> getSortedStudents(int sortType) {
        List<StudentWithCourses> students = db.studentWithCoursesDao().getSortedOtherStudents();
        switch(sortType) {
            case THIS_Q:
                for(StudentWithCourses swc : students) {
                    swc.student.thisQuarterScore = calculateThisQuarterScore(swc);
                }
                return db.studentWithCoursesDao().getSortedOtherStudentsByThisQuarter();
            case CLASS_SIZE:
                for(StudentWithCourses swc : students) {
                    swc.student.sizeScore = calculateSizeScore(swc);
                    db.studentWithCoursesDao().updateStudent(swc.student);
                }
                return db.studentWithCoursesDao().getSortedOtherStudentsByCourseSize();
            case RECENCY:
                for(StudentWithCourses swc : students) {
                    swc.student.recencyScore = calculateRecencyScore(swc);
                    db.studentWithCoursesDao().updateStudent(swc.student);
                }
                return db.studentWithCoursesDao().getSortedOtherStudentsByRecency();
            case WAVE_FROM:
                List<StudentWithCourses> retList = db.studentWithCoursesDao().getStudentsWhoWaved(true);
                retList.addAll(db.studentWithCoursesDao().getStudentsWhoWaved(false));
                return retList;
            default:
                for(StudentWithCourses swc : students) {
                    swc.student.commonCourses = calculateSharedCourseCount(swc);
                    db.studentWithCoursesDao().updateStudent(swc.student);
                }
                return db.studentWithCoursesDao().getSortedOtherStudents();
        }
    }
}
