package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.AssessmentDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAssessmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentDetailFragment extends Fragment {
    private static final String ID = "assessment_id";
    private OnAssessmentInteractionListener mListener;
    private AssessmentRO assessment;
    private AssessmentDAO dao;

    public AssessmentDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TermDetailFragment.
     */
    public static AssessmentDetailFragment newInstance(int id) {
        AssessmentDetailFragment fragment = new AssessmentDetailFragment();
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
        View root = Objects.requireNonNull(inflater).inflate(R.layout.fragment_assessment_detail,
                container,
                false);

        assert this.getArguments() != null;
        int id = this.getArguments().getInt(ID);

        TextView termTitleTextView = root.findViewById(R.id.courseTitleTextView);
        TextView goalTextView = root.findViewById(R.id.goalTextView);
        TextView typeTextView = root.findViewById(R.id.typeTextView);

        final ImageView deleteImageView = root.findViewById(R.id.deleteAssessmentImageView);
        final ImageView editAssessmentImageView = root.findViewById(R.id.editAssessmentImageView);

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar deleteSnackBar = Snackbar.make(deleteImageView,
                        "Delete Assessment: " + assessment.getTitle() + "?",
                        Snackbar.LENGTH_LONG);
                deleteSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AssessmentDAO assessmentDAO = new AssessmentDAO(getContext());

                        try {
                            List<AssessmentRO> assessments =
                                    assessmentDAO.getAllAssessmentsForCourse(assessment.getId());
                            for (AssessmentRO a : assessments) {
                                assessmentDAO.disassociate(a);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dao.delete(assessment.getId());

                        mListener.goToHomePage();
                    }
                }).show();
            }
        });

        dao = new AssessmentDAO(getContext());
        try {
            assessment = dao.get(id);
            termTitleTextView.setText(assessment.getTitle());
            typeTextView.setText(assessment.getType().toString());

            if (assessment.getGoalDate() != null) {
                goalTextView.setText(DateUtils.convertTimestampToString(assessment.getGoalDate()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        editAssessmentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editAssessment(assessment.getId());
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
    public interface OnAssessmentInteractionListener {
        void goToHomePage();

        void editAssessment(int id);
    }
}
