package com.school.codes.ric.mobileappsproject.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID = "term_id";
    private OnFragmentInteractionListener mListener;
    private TermRO term;
    private TermDAO dao;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(int id) {
        DetailFragment fragment = new DetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        int id = this.getArguments().getInt(ID);

        TextView termTitleTextView = root.findViewById(R.id.termTitleTextView);
        TextView termStartTextView = root.findViewById(R.id.termStartTextView);
        TextView termEndTextView = root.findViewById(R.id.termEndTextView);
        final ImageView deleteTermImageView = root.findViewById(R.id.deleteTermImageView);
        deleteTermImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar deleteSnackBar = Snackbar.make(deleteTermImageView,
                        "Delete " + term.getTitle() + "?",
                        Snackbar.LENGTH_LONG);
                deleteSnackBar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dao.delete(term.getId());
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i); //todo: attach homepage fragment instead
                    }
                }).show();
            }
        });

        dao = new TermDAO(getContext());
        try {
            term = dao.get(id);
            termTitleTextView.setText(term.getTitle());
            termStartTextView.setText(DateUtils.convertTimestampToString(term.getStart()));
            termEndTextView.setText(DateUtils.convertTimestampToString(term.getEnd()));
        } catch (ParseException e) {
            e.printStackTrace(); //todo: handle this
        }

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
