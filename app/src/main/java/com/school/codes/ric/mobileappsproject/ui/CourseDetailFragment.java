package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
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
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailFragment extends Fragment {
    private static final String ID = "course_id";
    private OnFragmentInteractionListener mListener;
    private CourseRO course;
    private CourseDAO dao;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TermDetailFragment.
     */
    public static CourseDetailFragment newInstance(int id) {
        CourseDetailFragment fragment = new CourseDetailFragment();
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
        View root = Objects.requireNonNull(inflater).inflate(R.layout.fragment_course_detail,
                container,
                false);

        assert this.getArguments() != null;
        int id = this.getArguments().getInt(ID);

        TextView termTitleTextView = root.findViewById(R.id.courseTitleTextView);
        TextView termStartTextView = root.findViewById(R.id.courseStartTextView);
        TextView termEndTextView = root.findViewById(R.id.courseEndTextView);

        TextView notesTextView = root.findViewById(R.id.notesTextView);
        TextView statusTextView = root.findViewById(R.id.statusTextView);
        TextView mentorTextView = root.findViewById(R.id.mentorTextView);
        TextView alertEndTextView = root.findViewById(R.id.alertEndTextView);
        TextView alertStartTextView = root.findViewById(R.id.alertStartTextView);

        final ImageView deleteImageView = root.findViewById(R.id.deleteTermImageView);
        final ImageView editTermImageView = root.findViewById(R.id.deleteAssessmentImageView);
        final ImageView share = root.findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, course.getNotes());
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar deleteSnackBar = Snackbar.make(deleteImageView,
                        "Delete Course: " + course.getTitle() + "?",
                        Snackbar.LENGTH_LONG);

                deleteSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            dao.delete(course.getId());
                            mListener.goToHomePage();
                        } catch (SQLiteConstraintException e) {
                            final Snackbar s = Snackbar.make(view,
                                    "Please Remove ALL Assessments before deleting",
                                    Snackbar.LENGTH_INDEFINITE);
                            s.setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    s.dismiss();
                                }
                            }).show();
                        }

                    }

                }).show();
            }
        });

        dao = new CourseDAO(getContext());
        try {
            course = dao.get(id);
            termTitleTextView.setText(course.getTitle());
            termStartTextView.setText(DateUtils.convertTimestampToString(course.getStart()));
            termEndTextView.setText(DateUtils.convertTimestampToString(course.getEnd()));
            notesTextView.setText(course.getNotes());
            statusTextView.setText(course.getStatus());
            mentorTextView.setText(course.getMentor());
            alertStartTextView.setText(DateUtils.convertTimestampToString(course.getStartAlert()));
            alertEndTextView.setText(DateUtils.convertTimestampToString(course.getEndAlert()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        editTermImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editCourse(course.getId());
            }
        });

        return root;
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
        void goToHomePage();

        void editCourse(int id);
    }
}
