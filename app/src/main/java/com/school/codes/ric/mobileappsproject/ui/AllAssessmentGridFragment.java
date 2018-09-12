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
import com.school.codes.ric.mobileappsproject.data.AssessmentDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAssessmentGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAssessmentGridFragment extends Fragment
        implements AssessmentGridAdapter.OnAssessmentClickListener {


    private OnAssessmentInteractionListener mListener;

    public AllAssessmentGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseGridFragment.
     */
    public static AllAssessmentGridFragment newInstance() {
        AllAssessmentGridFragment fragment = new AllAssessmentGridFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.assessment_fragment_grid, container, false);

        LinearLayout associateLayout = root.findViewById(R.id.associateLayout);
        associateLayout.setVisibility(View.INVISIBLE);
        TextView addCourses = root.findViewById(R.id.addCourses);
        TextView assessmentsText = root.findViewById(R.id.assessments);
        assessmentsText.setVisibility(View.INVISIBLE);
        addCourses.setVisibility(View.INVISIBLE);
        List<AssessmentRO> assessments = new ArrayList<>();

        try {
            AssessmentDAO assessmentDAO = new AssessmentDAO(getContext());
            assessments = assessmentDAO.getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (assessments.size() > 0) {
            assessmentsText.setVisibility(View.VISIBLE);
            assessmentsText.setText("All Assessments");
            AssessmentGridAdapter gridAdapter = new AssessmentGridAdapter(assessments, this);
            RecyclerView allAssessments = root.findViewById(R.id.associatedCoursesRecycler);
            allAssessments.setAdapter(gridAdapter);

            RecyclerView.LayoutManager layoutManager =
                    new GridLayoutManager(getContext(), 2);
            allAssessments.setLayoutManager(layoutManager);
        } else {
            assessmentsText.setVisibility(View.VISIBLE);
            assessmentsText.setText("Click + to Add an Assessment");
            Toast.makeText(getContext(),
                    "Please create an Assessment First",
                    Toast.LENGTH_LONG).show();
        }


        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAssessmentInteractionListener) {
            mListener = (OnAssessmentInteractionListener) context;
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
    public void assessmentClicked(int id) {
        mListener.onAssessmentClicked(id);
    }

    public interface OnAssessmentInteractionListener {
        void onAssessmentClicked(int id);
    }

}
