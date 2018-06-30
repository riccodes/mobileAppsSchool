package com.school.codes.ric.mobileappsproject.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter {

    private List<CourseRO> items;

    public GridAdapter(List<CourseRO> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,
                parent, false);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GridViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView startTextView;
        private TextView endTextView;
        private TextView extraTextView;

        public GridViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            startTextView = itemView.findViewById(R.id.startTextView);
            endTextView = itemView.findViewById(R.id.endTextView);
            extraTextView = itemView.findViewById(R.id.extraTextView);

        }

        private void bindView(int position) {
            CourseRO item = items.get(position);

            titleTextView.setText(item.getTitle());
            startTextView.setText(DateUtils.convertTimestampToString(item.getStart()));
            endTextView.setText(DateUtils.convertTimestampToString(item.getEnd()));
            extraTextView.setText(item.getStatus());
        }

    }
}
