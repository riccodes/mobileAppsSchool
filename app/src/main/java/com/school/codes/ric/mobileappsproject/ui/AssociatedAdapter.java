package com.school.codes.ric.mobileappsproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;

import java.util.List;

public class AssociatedAdapter extends RecyclerView.Adapter<AssociatedAdapter.ViewHolder> {

    private List<CourseRO> items;
    private TermRO parent;

    public AssociatedAdapter(List<CourseRO> items, TermRO parent) {
        this.items = items;
        this.parent = parent;
    }

    @NonNull
    @Override
    public AssociatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        return new ViewHolder(LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.associated_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssociatedAdapter.ViewHolder holder, int position) {

        CourseRO current = items.get(position);
        holder.associatedTitleTextView.setText(current.getTitle());

        if (current.getTermId() == parent.getId()) {
            associateView(holder);
        } else {
            disassociateView(holder);
        }

    }

    private void disassociateView(ViewHolder holder) {
        holder.associatedStatusTextView.setText("");
        holder.associatedTitleTextView.
                setTextColor(holder.associatedTitleTextView
                        .getResources()
                        .getColor(R.color.textPrimary, null));
        holder.associatedStatusTextView.
                setTextColor(holder.associatedStatusTextView
                        .getResources()
                        .getColor(R.color.textPrimary, null));
    }

    private void associateView(ViewHolder holder) {
        holder.associatedStatusTextView.setText("ADDED");
        holder.associatedTitleTextView.
                setTextColor(holder.associatedTitleTextView
                        .getResources()
                        .getColor(R.color.textSecondary, null));
        holder.associatedStatusTextView.
                setTextColor(holder.associatedStatusTextView
                        .getResources()
                        .getColor(R.color.textSecondary, null));
    }

    public List<CourseRO> getItems() {
        return items;
    }

    public void setItems(List<CourseRO> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView associatedTitleTextView;
        private TextView associatedStatusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            associatedTitleTextView = itemView.findViewById(R.id.associatedTitleTextView);
            associatedStatusTextView = itemView.findViewById(R.id.associatedStatusTextView);
        }

    }
}
