package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.birdsofafeather.model.db.AppDatabase;
import com.example.birdsofafeather.model.db.StudentWithCourses;

import java.util.List;

/**
 * Adapter used to create repeated UI views of students found while searching.
 */
public class StudentsViewAdapter extends RecyclerView.Adapter<StudentsViewAdapter.ViewHolder> {

    public static final String STUDENT_ID_EXTRA = "student_id";
    public static final String COMMON_COURSES_EXTRA = "common_courses";

    private static final String TAG = "BoaF_StudentsViewAdapter";

    private List<StudentWithCourses> students;
    private View view;

    /**
     * @param students A list of non-user students, sorted from most to least courses shared with the user.
     */
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

    /**
     * @param students A list of non-user students, sorted from most to least courses shared with the user.
     */
    public void addStudent(List<StudentWithCourses> students, Context context) {
        // We don't know where the new student was inserted, so refresh everything.
        this.students = students;
        ((Activity) context).runOnUiThread(() -> notifyDataSetChanged());
    }

    /**
     * Custom ViewHolder for individual students with shared classes found while searching.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView studentNameView;
        private final ImageView studentPfpView;
        private final TextView matchedCoursesView;
        private final CheckBox favoriteCheckbox;
        private final ImageButton handWaveButton;

        private StudentWithCourses student;

        ViewHolder(View itemView) {
            super(itemView);
            this.studentNameView = itemView.findViewById(R.id.stud_name_textview);
            this.studentPfpView = itemView.findViewById(R.id.stud_pfp_imageview);
            this.matchedCoursesView = itemView.findViewById(R.id.num_matched_courses_textview);
            this.favoriteCheckbox = itemView.findViewById(R.id.favorite_checkbox);
            this.favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AppDatabase.singleton(studentNameView.getContext())
                            .studentWithCoursesDao().favoriteStudent(student.getId(), isChecked);
                }
            });
            this.handWaveButton = itemView.findViewById(R.id.filled_hand_wave_id);

            itemView.setOnClickListener(this);
        }

        /**
         * Sets various UI components of a found student.
         *
         * @param student
         */
        public void setPerson(StudentWithCourses student) {
            Log.d(TAG, "Set person " + student.getName());
            this.student = student;
            this.studentNameView.setText(student.getName());
            this.matchedCoursesView.setText(itemView.getContext().getString(R.string.matched_courses, this.student.getCommonCourseCount()));
            Glide.with(itemView)
                    .load(student.getPhotoURL())
                    .fitCenter()
                    .into(studentPfpView);
            this.favoriteCheckbox.setChecked(student.getFavorite());
            if(student.student.waveToMe) {
                this.handWaveButton.setVisibility(View.VISIBLE);
            } else {
                this.handWaveButton.setVisibility(View.GONE);
            }
        }

        /**
         * onClick handler for displaying particular student's details pages.
         * @param view
         */
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
