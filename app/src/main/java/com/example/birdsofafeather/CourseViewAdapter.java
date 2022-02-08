package com.example.birdsofafeather;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder>{
    private final List<String> courses;

    public CourseViewAdapter(List<String> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);

        return new CourseViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.ViewHolder holder, int position) {
        holder.setNote(courses.get(position));
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseTextView = itemView.findViewById(R.id.course_name_textview);
        }

        public void setNote(String course) {
            this.courseTextView.setText(course);
        }
    }
}
