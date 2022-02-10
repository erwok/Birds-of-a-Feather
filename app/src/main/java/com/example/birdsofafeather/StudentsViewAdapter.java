package com.example.birdsofafeather;

import android.app.Activity;
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
import com.example.birdsofafeather.model.db.Student;

import java.util.List;

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {
    private List<? extends IStudent> students;
    private List<Integer> commCourses;
    private View view;

    public StudentsViewAdapter(List<? extends IStudent> students, List<Integer> commCourses) {
        super();
        this.students = students;
        this.commCourses = commCourses;
    }

    @NonNull
    @Override
    public StudentsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater
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

    public void addStudent(List<IStudent> students, List<Integer> commCourses) {
        this.students = students;
        this.commCourses = commCourses;
        ((Activity) view.getContext()).runOnUiThread(new Runnable() {
           @Override
           public void run() {
               notifyItemRangeChanged(0, students.size());
           }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView studNameView;
        private final ImageView studPfpView;
        private final TextView matchedCoursesView;
        private IStudent student;
        private int commonCourses;

        private final String MATCHED_COURSES = "Matched Courses: ";

        ViewHolder(View itemView) {
            super(itemView);
            this.studNameView = itemView.findViewById(R.id.stud_name_textview);
            this.studPfpView = itemView.findViewById(R.id.stud_pfp_imageview);
            this.matchedCoursesView = itemView.findViewById(R.id.course_name_textview);
            itemView.setOnClickListener(this);
        }

        public void setPerson(IStudent student, int commCourses) {
            this.student = student;
            this.studNameView.setText(student.getName());
            this.commonCourses = commCourses;
            this.matchedCoursesView.setText(MATCHED_COURSES + commonCourses);
            // pfp not implemented yet
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra("student_id", this.student.getId());
            intent.putExtra("comm_courses", commonCourses);
            context.startActivity(intent);
        }
    }
}
