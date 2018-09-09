package com.school.codes.ric.mobileappsproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.util.List;

public class AssessmentGridAdapter extends RecyclerView.Adapter {

    private List<AssessmentRO> assessments;
    private OnAssessmentClickListener mListener;

    AssessmentGridAdapter(List<AssessmentRO> items, OnAssessmentClickListener listener) {
        this.assessments = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_grid_item,
                parent, false);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GridViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public List<AssessmentRO> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentRO> assessments) {
        this.assessments = assessments;
    }

    public interface OnAssessmentClickListener {
        void assessmentClicked(int id);
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView goalTextView;
        private AssessmentRO assessment;

        GridViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            goalTextView = itemView.findViewById(R.id.goalTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.assessmentClicked(assessment.getId());
                }
            });

        }

        private void bindView(int position) {
            assessment = getAssessments().get(position);
            titleTextView.setText(assessment.getTitle());
            goalTextView.setText(DateUtils.convertTimestampToString(assessment.getGoalDate()));

        }

    }
}
