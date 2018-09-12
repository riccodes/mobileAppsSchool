package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseGridFragment extends Fragment implements CourseGridAdapter.OnCourseClickListener {

    private static final String ID = "course_id";
    private OnCourseInteractionListener mListener;
    private int mId;
    private LinearLayout associateLayout;
    private boolean associating = false;
    private RecyclerView associatedCoursesRecycler;
    private CourseGridAdapter gridAdapter;
    private CourseDAO courseDAO;

    public CourseGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseGridFragment.
     */
    public static CourseGridFragment newInstance(int id) {
        CourseGridFragment fragment = new CourseGridFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.course_fragment_grid, container, false);

        associateLayout = root.findViewById(R.id.associateLayout);
        associateLayout.setVisibility(View.INVISIBLE);
        TextView addCourses = root.findViewById(R.id.addCourses);
        List<CourseRO> courses = new ArrayList<>();

        try {
            courseDAO = new CourseDAO(getContext());
            courses = courseDAO.getAllAssociated(mId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        gridAdapter = new CourseGridAdapter(courses, this);
        associatedCoursesRecycler = root.findViewById(R.id.associatedCoursesRecycler);
        associatedCoursesRecycler.setAdapter(gridAdapter);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), 2);
        associatedCoursesRecycler.setLayoutManager(layoutManager);

        courseDAO = new CourseDAO(getContext());
        TermDAO termDAO = new TermDAO(getContext());

        try {
            RecyclerView associateCoursesRecycler =
                    root.findViewById(R.id.associateNewCoursesRecycler);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            AssociateCourseAdapter associateAdapter = new AssociateCourseAdapter(courseDAO.getAll(),
                    termDAO.get(mId),
                    associateCoursesRecycler,
                    getContext());
            associateAdapter.notifyDataSetChanged();
            associateCoursesRecycler.setLayoutManager(manager);
            associateCoursesRecycler.setAdapter(associateAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        addCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (associating) {
                    try {
                        associatedCoursesRecycler.setVisibility(View.VISIBLE);
                        associateLayout.setVisibility(View.INVISIBLE);

                        gridAdapter.getCourses().clear();
                        gridAdapter.setCourses(courseDAO.getAllAssociated(mId));
                        gridAdapter.notifyDataSetChanged();

                        associating = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    associatedCoursesRecycler.setVisibility(View.INVISIBLE);
                    associateLayout.setVisibility(View.VISIBLE);
                    associating = true;
                }
            }
        });

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCourseInteractionListener) {
            mListener = (OnCourseInteractionListener) context;
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

    @Override
    public void courseClicked(int id) {
        mListener.onCourseClicked(id);
    }

    public interface OnCourseInteractionListener {
        void onCourseClicked(int id);
    }

}
