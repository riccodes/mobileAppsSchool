package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCoursesGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCoursesGridFragment extends Fragment
        implements CourseGridAdapter.OnCourseClickListener {


    private OnCourseInteractionListener mListener;

    public AllCoursesGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseGridFragment.
     */
    public static AllCoursesGridFragment newInstance() {
        return new AllCoursesGridFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.course_fragment_grid, container, false);

        LinearLayout associateLayout = root.findViewById(R.id.associateLayout);
        associateLayout.setVisibility(View.INVISIBLE);
        TextView addCourses = root.findViewById(R.id.addCourses);
        TextView assessmentsText = root.findViewById(R.id.assessments);
        assessmentsText.setVisibility(View.INVISIBLE);
        addCourses.setVisibility(View.INVISIBLE);
        List<CourseRO> courses = new ArrayList<>();

        try {
            CourseDAO courseDAO = new CourseDAO(getContext());
            courses = courseDAO.getAll();
        } catch (ParseException e) {
            e.printStackTrace(); //todo: handle this properly
        }

        if (courses.size() > 0) {
            assessmentsText.setVisibility(View.VISIBLE);
            assessmentsText.setText("All Courses");
            CourseGridAdapter gridAdapter = new CourseGridAdapter(courses, this);
            RecyclerView allAssessments = root.findViewById(R.id.associatedCoursesRecycler);
            allAssessments.setAdapter(gridAdapter);

            RecyclerView.LayoutManager layoutManager =
                    new GridLayoutManager(getContext(), 2);
            allAssessments.setLayoutManager(layoutManager);
        } else {
            assessmentsText.setVisibility(View.VISIBLE);
            assessmentsText.setText("Click + to Add a Course");
            Toast.makeText(getContext(),
                    "Please create a Course First",
                    Toast.LENGTH_LONG).show();
        }


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

    public void courseClicked(int id) {
        mListener.onCourseClicked(id);
    }

    public interface OnCourseInteractionListener {
        void onCourseClicked(int id);
    }

}
