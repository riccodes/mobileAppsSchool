package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;

import java.text.ParseException;
import java.util.List;

public class AssociateCourseAdapter extends RecyclerView.Adapter<AssociateCourseAdapter.ViewHolder> {

    private List<CourseRO> items;
    private TermRO parent;
    private Context context;
    private TextView associatedTitleTextView;

    AssociateCourseAdapter(List<CourseRO> items,
                           TermRO parent,
                           RecyclerView recyclerView,
                           Context context) {
        this.items = items;
        this.parent = parent;
        this.context = context;
        initSwipe(this, recyclerView);

    }

    @NonNull
    @Override
    public AssociateCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                int viewType) {
        return new ViewHolder(LayoutInflater.
                from(parent.getContext())
                .inflate(R.layout.associated_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssociateCourseAdapter.ViewHolder holder, int position) {
        CourseRO current = items.get(position);
        associatedTitleTextView = holder.associatedTitleTextView;
        associatedTitleTextView.setText(current.getTitle());

        if (current.getTermId() == parent.getId()) {
            associateView(holder);
        } else {
            disassociateView(holder);
        }

    }

    private void initSwipe(final AssociateCourseAdapter adapter,
                           final RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback itemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    CourseDAO courseDAO = new CourseDAO(context);

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.RIGHT ||
                                direction == ItemTouchHelper.LEFT) {

                            CourseRO course = getItems().get(position);

                            if (course.getTermId() == parent.getId()) {
                                courseDAO.disassociate(course);
                            } else {
                                course.setTermId(parent.getId());
                                courseDAO.update(course);
                            }

                            resetAdapter();

                        }

                    }

                    private void resetAdapter() {
                        try {
                            adapter.getItems().clear();
                            adapter.setItems(courseDAO.getAll());
                            adapter.notifyDataSetChanged();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                            int actionState, boolean isCurrentlyActive) {
                        drawActions(c, viewHolder, dX, actionState);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    /**
     * Handles the animations that occur when the cards
     * are swiped
     *
     * @param c           The canvas needed for painting
     * @param viewHolder  the view holder associated with the swipes
     * @param dX          direction
     * @param actionState state
     */
    private void drawActions(Canvas c,
                             RecyclerView.ViewHolder viewHolder,
                             float dX,
                             int actionState) {

        Paint p = new Paint();

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;

            if (dX > 0) {

                p.setColor(Color.parseColor("#ff4081"));
                RectF background = new RectF((float) itemView.getLeft(),
                        (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                Paint p2 = new Paint();
                p2.setColor(Color.parseColor("#ffffff"));
                c.drawText("+", 0, 0, p2);
//                icon = BitmapFactory.decodeResource(getResources(), R.drawable.check_small);
//                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (
//                        float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (
//                        float) itemView.getBottom() - width);
//                c.drawBitmap(icon, null, icon_dest, p);
            } else {
                p.setColor(Color.parseColor("#ff4081"));
                RectF background = new RectF((float) itemView.getRight() + dX, (
                        float) itemView.getTop(), (float) itemView.getRight(),
                        (float) itemView.getBottom());
                c.drawRect(background, p);
            }
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
        holder.associatedStatusTextView.setText(R.string.added);
        holder.associatedTitleTextView.
                setTextColor(holder.associatedTitleTextView
                        .getResources()
                        .getColor(R.color.textAdded, null));
        holder.associatedStatusTextView.
                setTextColor(holder.associatedStatusTextView
                        .getResources()
                        .getColor(R.color.textAdded, null));
    }

    private List<CourseRO> getItems() {
        return items;
    }

    private void setItems(List<CourseRO> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView associatedTitleTextView;
        private TextView associatedStatusTextView;

        ViewHolder(View itemView) {
            super(itemView);
            associatedTitleTextView = itemView.findViewById(R.id.associatedTitleTextView);
            associatedStatusTextView = itemView.findViewById(R.id.associatedStatusTextView);
        }

    }
}
