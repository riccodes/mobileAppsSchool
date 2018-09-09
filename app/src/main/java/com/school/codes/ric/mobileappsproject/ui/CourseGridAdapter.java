package com.school.codes.ric.mobileappsproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.util.List;

public class CourseGridAdapter extends RecyclerView.Adapter {

    private List<CourseRO> courses;
    private OnCourseClickListener mListener;

    CourseGridAdapter(List<CourseRO> items, OnCourseClickListener onCourseClickListener) {
        this.courses = items;
        this.mListener = onCourseClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_grid_item,
                parent, false);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GridViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public List<CourseRO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseRO> courses) {
        this.courses = courses;
    }

    public interface OnCourseClickListener {
        void courseClicked(int id);
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = GridViewHolder.class.getSimpleName();
        private TextView titleTextView;
        private TextView startTextView;
        private TextView endTextView;
        private TextView extraTextView;
        private CourseRO course;

        GridViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            startTextView = itemView.findViewById(R.id.goal);
            endTextView = itemView.findViewById(R.id.endTextView);
            extraTextView = itemView.findViewById(R.id.extraTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Course title=" + course.getTitle());
                    mListener.courseClicked(course.getId());
                }
            });

        }

        private void bindView(int position) {
            course = getCourses().get(position);
            titleTextView.setText(course.getTitle());
            startTextView.setText(DateUtils.convertTimestampToString(course.getStart()));
            endTextView.setText(DateUtils.convertTimestampToString(course.getEnd()));
            extraTextView.setText(course.getStatus());
        }

    }
}
