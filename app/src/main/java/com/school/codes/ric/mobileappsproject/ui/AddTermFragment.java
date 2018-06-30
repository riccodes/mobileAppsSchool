package com.school.codes.ric.mobileappsproject.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTermFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTermFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTermFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private List<CourseRO> tempCourses = new ArrayList<>();
    private CourseDAO courseDAO;
    private TermRO term;

    private TextView startDate;
    private TextView endDate;

    private Timestamp start;
    private Timestamp end;

    public AddTermFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTermFragment.
     */
    public static AddTermFragment newInstance() {
        AddTermFragment fragment = new AddTermFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            TERM_ID = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        courseDAO = new CourseDAO(getContext());
        final TermDAO termDAO = new TermDAO(getContext());
        View v = inflater.inflate(R.layout.fragment_add_term, container, false);

        final EditText titleEditText = v.findViewById(R.id.titleEditText);
        startDate = v.findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener startDateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                start = new Timestamp(year - 1900, month, dayOfMonth, 0, 0, 0, 0);
                                startDate.setText(DateUtils.convertTimestampToString(start));
                            }
                        };
                new DatePickerDialog(getActivity(),
                        startDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = v.findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener endDateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                end = new Timestamp(year - 1900, month, dayOfMonth, 0, 0, 0, 0);
                                endDate.setText(DateUtils.convertTimestampToString(end));
                            }
                        };
                new DatePickerDialog(getActivity(),
                        endDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button cancelButton = v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (CourseRO c : tempCourses) {
                    courseDAO.disassociate(c);
                }

                termDAO.delete(termDAO.getLastId());

                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i); //todo: attach homepage fragment instead
            }
        });

        Button saveButton = v.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (titleEditText.getText().toString().isEmpty() ||
                        titleEditText.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Title cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else if (start == null) {
                    Toast.makeText(getContext(), "Start date cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else if (end == null) {
                    Toast.makeText(getContext(), "End date cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    term.setTitle(titleEditText.getText().toString());
                    term.setStart(start);
                    term.setEnd(end);

                    termDAO.update(term);

                    Toast.makeText(getContext(), "Term created successfully", Toast.LENGTH_SHORT)
                            .show();

                    onTermSave(term.getId());
                }

            }
        });

        try {
            term = new TermRO("",
                    DateUtils.convertStringToTimestamp("01-Jan-1990"),
                    DateUtils.convertStringToTimestamp("01-Jan-1990"));
            termDAO.add(term);
            term = termDAO.get(termDAO.getLastId());

            RecyclerView recyclerView = v.findViewById(R.id.associatedCoursesRecycler);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            AssociatedAdapter adapter = new AssociatedAdapter(courseDAO.getAll(), term);
            initSwipe(adapter, recyclerView);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);

        } catch (ParseException e) {
            e.printStackTrace(); //todo: handle this
            return null;
        }

        return v;
    }

    private void initSwipe(final AssociatedAdapter adapter,
                           final RecyclerView recyclerView) {

        ItemTouchHelper.SimpleCallback itemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.RIGHT ||
                                direction == ItemTouchHelper.LEFT) {

                            CourseRO course = adapter.getItems().get(position);

                            if (course.getTermId() == term.getId()) {
                                courseDAO.disassociate(course);
                                tempCourses.remove(course);
                            } else {
                                course.setTermId(term.getId());
                                courseDAO.update(course);
                                tempCourses.add(course);
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
//        Bitmap icon;
        Paint p = new Paint();

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;

            if (dX > 0) {

                p.setColor(Color.parseColor("#ff4081"));
                RectF background = new RectF((float) itemView.getLeft(),
                        (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                c.drawText("+", 0, 0, p);
//                icon = BitmapFactory.decodeResource(getResources(), R.drawable.check_small);
//                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (
//                        float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (
//                        float) itemView.getBottom() - width);
//                c.drawBitmap(icon, null, icon_dest, p);
            } else {
                p.setColor(Color.parseColor("#ff4081"));
                RectF background = new RectF((float) itemView.getLeft(),
                        (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onTermSave(int id) {
        if (mListener != null) {
            mListener.onTermFinalized(id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onTermFinalized(int termId);
    }
}
