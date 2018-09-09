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
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.List;
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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

        TextView termTitleTextView = root.findViewById(R.id.termTitleTextView);
        TextView termStartTextView = root.findViewById(R.id.termStartTextView);
        TextView termEndTextView = root.findViewById(R.id.termEndTextView);

        TextView notesTextView = root.findViewById(R.id.notesTextView);
        TextView statusTextView = root.findViewById(R.id.statusTextView);
        TextView mentorTextView = root.findViewById(R.id.mentorTextView);
        TextView alertEndTextView = root.findViewById(R.id.alertEndTextView);
        TextView alertStartTextView = root.findViewById(R.id.alertStartTextView);

        final ImageView deleteImageView = root.findViewById(R.id.deleteTermImageView);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar deleteSnackBar = Snackbar.make(deleteImageView,
                        "Delete Course: " + course.getTitle() + "?",
                        Snackbar.LENGTH_LONG);
                deleteSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CourseDAO courseDAO = new CourseDAO(getContext());

                        try {
                            List<CourseRO> courses =
                                    courseDAO.getAllAssociated(course.getId());
                            for (CourseRO c : courses) {
                                courseDAO.disassociate(c);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dao.delete(course.getId());

                        mListener.goToHomePage();
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
            e.printStackTrace(); //todo: handle this
        }

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
    }
}
