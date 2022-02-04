package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.IStudent;

import java.util.List;

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {
    private final List<? extends IStudent> students;
    private final List<Integer> commCourses;

    public StudentsViewAdapter(List<? extends IStudent> students, List<Integer> commCourses) {
        super();
        this.students = students;
        this.commCourses = commCourses;
    }

    @NonNull
    @Override
    public StudentsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewAdapter.ViewHolder holder, int position) {
        holder.setPerson(students.get(position), commCourses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView studNameView;
        private final ImageView studPfpView;
        private final TextView matchedCoursesView;
        private IStudent student;

        private final String MATCHED_COURSES = "Matched Courses: ";

        ViewHolder(View itemView) {
            super(itemView);
            this.studNameView = itemView.findViewById(R.id.stud_name_textview);
            this.studPfpView = itemView.findViewById(R.id.stud_pfp_imageview);
            this.matchedCoursesView = itemView.findViewById(R.id.matched_courses_textview);
            itemView.setOnClickListener(this);
        }

        public void setPerson(IStudent student, int commCourses) {
            this.student = student;
            this.studNameView.setText(student.getName());
            this.matchedCoursesView.setText(MATCHED_COURSES + commCourses);
            // pfp not implemented yet
        }

        @Override
        public void onClick(View view) {
            // does nothing for now, but eventually should pull up new activity with list
            // of common courses
        }
    }
}
