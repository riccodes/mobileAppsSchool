package com.school.codes.ric.mobileappsproject.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTermFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTermFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTermFragment extends Fragment {

    private static final String ID = "ID";

    private OnFragmentInteractionListener mListener;
    private CourseDAO courseDAO;
    private TermRO term;

    private TextView startDate;
    private TextView endDate;

    private Timestamp start;
    private Timestamp end;
    private boolean isNewTerm;

    public AddTermFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTermFragment.
     */
    public static AddTermFragment newInstance(int id) {
        AddTermFragment fragment = new AddTermFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                new DatePickerDialog(Objects.requireNonNull(getActivity()),
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
                new DatePickerDialog(Objects.requireNonNull(getActivity()),
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

                if (isNewTerm) {
                    List<CourseRO> tempCourses = null;
                    try {
                        tempCourses = courseDAO.getAllAssociated(term.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    assert tempCourses != null;
                    for (CourseRO c : tempCourses) {
                        courseDAO.disassociate(c);
                    }

                    termDAO.delete(termDAO.getLastId());
                }

                goToHomePage();
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

            assert getArguments() != null;
            if (getArguments().getInt(ID) == 0) {
                isNewTerm = true;

                term = new TermRO("",
                        DateUtils.convertStringToTimestamp("01-Jan-1990"),
                        DateUtils.convertStringToTimestamp("01-Jan-1990"));
                termDAO.add(term);
                term = termDAO.get(termDAO.getLastId());
            } else {
                isNewTerm = false;

                term = termDAO.get(getArguments().getInt(ID));
                titleEditText.setText(term.getTitle());
                end = term.getEnd();
                endDate.setText(DateUtils.convertTimestampToString(end));
                start = term.getStart();
                startDate.setText(DateUtils.convertTimestampToString(start));
            }


            setUpRecycler(v);

        } catch (ParseException e) {
            return null;
        }

        return v;
    }

    private void setUpRecycler(View v) throws ParseException {
        RecyclerView associateNewCoursesRecycler =
                v.findViewById(R.id.associateNewCoursesRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        AssociateCourseAdapter adapter = new AssociateCourseAdapter(courseDAO.getAll(),
                term,
                associateNewCoursesRecycler,
                getContext());

        associateNewCoursesRecycler.setLayoutManager(manager);
        associateNewCoursesRecycler.setAdapter(adapter);
    }

    public void onTermSave(int id) {
        if (mListener != null) {
            mListener.onTermFinalized(id);
        }
    }

    public void goToHomePage() {
        if (mListener != null) {
            mListener.goToHomePage();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCourseInteractionListener");
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

        void goToHomePage();
    }
}
