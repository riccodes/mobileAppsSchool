package com.school.codes.ric.mobileappsproject.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.AssessmentDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_TITLE_GOAL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAssessmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAssessmentFragment extends Fragment {

    private static final String TAG = AddAssessmentFragment.class.getSimpleName();
    private static final String ID = "ID";
    private OnAssessmentInteractionListener mListener;
    private AssessmentDAO assessmentDAO;
    private AssessmentRO assessment;

    private Timestamp goal;
    private Spinner typeSpinner;

    public AddAssessmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTermFragment.
     */
    public static AddAssessmentFragment newInstance(int id) {
        AddAssessmentFragment fragment = new AddAssessmentFragment();
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

        assessmentDAO = new AssessmentDAO(getContext());
        View v = inflater.inflate(R.layout.fragment_add_assessment, container, false);

        final EditText titleEditText = v.findViewById(R.id.titleEditText);
        final TextView goalDate = v.findViewById(R.id.goalDate);
        typeSpinner = v.findViewById(R.id.typeSpinner);

        List<String> statuses = new ArrayList<>();
        statuses.addAll(Arrays.asList(AssessmentRO.AssessmentType.OBJECTIVE.toString(),
                AssessmentRO.AssessmentType.PERFORMANCE.toString()));
        ArrayAdapter adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                statuses);
        typeSpinner.setAdapter(adapter);

        goalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener endDateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                goal = new Timestamp(year - 1900, month, dayOfMonth, 0, 0, 0, 0);
                                goalDate.setText(DateUtils.convertTimestampToString(goal));
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
                } else if (goal == null) {
                    Toast.makeText(getContext(), "Goal date cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    assessment = new AssessmentRO();
                    assessment.setTitle(titleEditText.getText().toString());
                    assessment.setGoalDate(goal);
                    assessment.setType(AssessmentRO.AssessmentType.valueOf(typeSpinner.getSelectedItem().toString()));
                    assessment.setGoalDate(goal);

                    assessmentDAO.add(assessment);

                    scheduleNotification(NOTIFICATION_TITLE_GOAL,
                            assessment.getTitle(),
                            assessment.getId(),
                            assessment.getGoalDate());

                    onCourseSave(assessmentDAO.getLastId());

                    Toast.makeText(getContext(), "Assessment created successfully", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        if (getArguments().getInt(ID) > 0) {
            try {
                assessment = assessmentDAO.get(getArguments().getInt(ID));
                titleEditText.setText(assessment.getTitle());
                goal = assessment.getGoalDate();
                goalDate.setText(DateUtils.convertTimestampToString(assessment.getGoalDate()));

                setSpinner(typeSpinner);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    private void scheduleNotification(String title, String content, int id, Timestamp alert) {


        // Set up intent to open TaskListFragment
        Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent mainActivityPendingIntent =
                PendingIntent.getActivity(getContext(), id, mainActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(getContext());
        notification.setContentTitle(title);
        notification.setContentText(content);
        notification.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notification.setSmallIcon(R.drawable.notify);
        notification.setAutoCancel(true);
        notification.setNumber(1);
        notification.setContentIntent(mainActivityPendingIntent);


        // create intent to add task values for notification
        Intent notificationIntent = new Intent(getContext(),
                NotifPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, id);
        notificationIntent.putExtra(NOTIFICATION, notification.build());

        // call alarm manager to schedule notification
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext()
                , id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =
                (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                alert.getTime(),
                pendingIntent);

    }

    private void setSpinner(Spinner typeSpinner) {
        String type = assessment.getType().name();
        int index;
        switch (type) {
            case "PERFORMANCE":
                index = 1;
                break;
            default:
                index = 0;
        }
        typeSpinner.setSelection(index);
    }

    public void onCourseSave(int id) {
        if (mListener != null) {
            mListener.onAssessmentFinalized(id);
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
        void onAssessmentFinalized(int termId);

        void goToHomePage();
    }
}
