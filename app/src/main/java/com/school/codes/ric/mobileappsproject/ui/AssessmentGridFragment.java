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
import com.school.codes.ric.mobileappsproject.data.AssessmentDAO;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentGridFragment extends Fragment
        implements AssessmentGridAdapter.OnAssessmentClickListener {

    private static final String ID = "assessment_id";
    private OnAssessmentInteractionListener mListener;
    private int mId;
    private LinearLayout associateLayout;
    private boolean associating = false;
    private RecyclerView associatedCoursesRecycler;
    private AssessmentGridAdapter gridAdapter;
    private AssessmentDAO assessmentDAO;

    public AssessmentGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseGridFragment.
     */
    public static AssessmentGridFragment newInstance(int id) {
        AssessmentGridFragment fragment = new AssessmentGridFragment();
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

        final View root = inflater.inflate(R.layout.assessment_fragment_grid, container, false);

        associateLayout = root.findViewById(R.id.associateLayout);
        associateLayout.setVisibility(View.INVISIBLE);
        final TextView addCourses = root.findViewById(R.id.addCourses);
        List<AssessmentRO> assessments = new ArrayList<>();

        try {
            assessmentDAO = new AssessmentDAO(getContext());
            assessments = assessmentDAO.getAllAssessmentsForCourse(mId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        gridAdapter = new AssessmentGridAdapter(assessments, this);
        associatedCoursesRecycler = root.findViewById(R.id.associatedCoursesRecycler);
        associatedCoursesRecycler.setAdapter(gridAdapter);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getContext(), 2);
        associatedCoursesRecycler.setLayoutManager(layoutManager);

        CourseDAO courseDAO = new CourseDAO(getContext());

        try {
            RecyclerView associateCoursesRecycler =
                    root.findViewById(R.id.associateNewCoursesRecycler);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            AssociateAssessmentAdapter associateAdapter = new AssociateAssessmentAdapter(assessmentDAO.getAll(),
                    courseDAO.get(mId),
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
                    addCourses.setText("+");
                    try {
                        associatedCoursesRecycler.setVisibility(View.VISIBLE);
                        associateLayout.setVisibility(View.INVISIBLE);

                        gridAdapter.getAssessments().clear();
                        gridAdapter.setAssessments(assessmentDAO.getAllAssessmentsForCourse(mId));
                        gridAdapter.notifyDataSetChanged();

                        associating = false;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    addCourses.setText("-");
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
