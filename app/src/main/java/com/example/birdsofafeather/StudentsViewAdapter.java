package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.StudentWithCourses;

import java.util.List;

public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {

    public static final String STUDENT_ID_EXTRA = "student_id";
    public static final String COMMON_COURSES_EXTRA = "common_courses";

    private static final String TAG = "BoaF_StudentsViewAdapter";

    private List<StudentWithCourses> students;
    private View view;

    public StudentsViewAdapter(List<StudentWithCourses> students) {
        super();
        this.students = students;
        Log.d(TAG, "Students length: " + students.size());
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
        holder.setPerson(students.get(position));
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public void addStudent(List<StudentWithCourses> students) {
        this.students = students;
        ((Activity) view.getContext()).runOnUiThread(() -> notifyItemRangeChanged(0, students.size()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView studentNameView;
        private final ImageView studentPfpView;
        private final TextView matchedCoursesView;

        private StudentWithCourses student;


        ViewHolder(View itemView) {
            super(itemView);
            this.studentNameView = itemView.findViewById(R.id.stud_name_textview);
            this.studentPfpView = itemView.findViewById(R.id.stud_pfp_imageview);
            this.matchedCoursesView = itemView.findViewById(R.id.course_name_textview);
            itemView.setOnClickListener(this);
        }

        public void setPerson(StudentWithCourses student) {
            Log.d(TAG, "Set person " + student.getName());
            this.student = student;
            this.studentNameView.setText(student.getName());
            this.matchedCoursesView.setText(itemView.getContext().getString(R.string.matched_courses, this.student.getCommonCourseCount()));
            // TODO implement PFP
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra(STUDENT_ID_EXTRA, this.student.getId());
            intent.putExtra(COMMON_COURSES_EXTRA, this.student.getCommonCourseCount());
            context.startActivity(intent);
        }
    }
}
