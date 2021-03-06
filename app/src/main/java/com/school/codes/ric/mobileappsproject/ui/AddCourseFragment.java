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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.Constants;
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
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_ID_OFFSET;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_TITLE_END;
import static com.school.codes.ric.mobileappsproject.util.Constants.NOTIFICATION_TITLE_START;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCourseFragment extends Fragment {

    public static final String TAG = AddCourseFragment.class.getSimpleName();
    private static final String ID = "ID";

    private OnFragmentInteractionListener mListener;
    private CourseDAO courseDAO;
    private AssessmentDAO assessmentDAO;

    private TextView startDate;
    private TextView endDate;
    private Timestamp start;
    private Timestamp startAlert;
    private Timestamp end;
    private Timestamp endAlert;
    private TextView startAlertDate;
    private TextView endAlertDate;
    private EditText notesEditText;
    private EditText mentorEditText;
    private CourseRO course;
    private EditText titleEditText;
    private boolean isNewCourse;

    public AddCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTermFragment.
     */
    public static AddCourseFragment newInstance(int id) {
        AddCourseFragment fragment = new AddCourseFragment();
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
        View v = inflater.inflate(R.layout.fragment_add_course, container, false);

        titleEditText = v.findViewById(R.id.titleEditText);
        startDate = v.findViewById(R.id.startDate);
        endDate = v.findViewById(R.id.endDate);
        endAlertDate = v.findViewById(R.id.endAlertDate);
        startAlertDate = v.findViewById(R.id.startAlertDate);
        notesEditText = v.findViewById(R.id.notesEditText);
        mentorEditText = v.findViewById(R.id.mentorEditText);
        final Spinner statusSpinner = v.findViewById(R.id.typeSpinner);

        List<String> statuses = new ArrayList<>();
        statuses.addAll(Arrays.asList(Constants.PLAN_STATUS,
                Constants.IN_PROGRESS_STATUS,
                Constants.COMPLETE_STATUS,
                Constants.DROPPED_STATUS));
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_expandable_list_item_1,
                statuses);
        statusSpinner.setAdapter(arrayAdapter);

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

        startAlertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener startDateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startAlert = new Timestamp(year - 1900, month, dayOfMonth, 0, 0, 0, 0);
                                startAlertDate.setText(DateUtils.convertTimestampToString(start));
                            }
                        };
                new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        startDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endAlertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener startDateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endAlert = new Timestamp(year - 1900, month, dayOfMonth, 0, 0, 0, 0);
                                endAlertDate.setText(DateUtils.convertTimestampToString(start));
                            }
                        };
                new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        startDateListener,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button cancelButton = v.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNewCourse) {
                    List<AssessmentRO> tempAssessments = null;

                    try {
                        tempAssessments = assessmentDAO.getAllAssessmentsForCourse(course.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    assert tempAssessments != null;
                    for (AssessmentRO a : tempAssessments) {
                        assessmentDAO.disassociate(a);
                    }

                    courseDAO.delete(courseDAO.getLastId());
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
                } else if (startAlert == null) {
                    Toast.makeText(getContext(), "Start Alert date cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else if (endAlert == null) {
                    Toast.makeText(getContext(), "Start Alert date cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else if (mentorEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Mentor info cannot be null", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    course.setTitle(titleEditText.getText().toString());
                    course.setStart(start);
                    course.setEnd(end);
                    course.setStartAlert(startAlert);
                    course.setEndAlert(endAlert);
                    course.setNotes(notesEditText.getText().toString());
                    course.setMentor(mentorEditText.getText().toString());
                    course.setStatus(statusSpinner.getSelectedItem().toString());

                    courseDAO.update(course);

                    scheduleNotification(NOTIFICATION_TITLE_START,
                            course.getTitle(),
                            course.getId(),
                            course.getStartAlert());

                    scheduleNotification(NOTIFICATION_TITLE_END,
                            course.getTitle(),
                            course.getId() + NOTIFICATION_ID_OFFSET,
                            course.getEndAlert());

                    onCourseSave(course.getId());

                    Toast.makeText(getContext(), "Course created successfully", Toast.LENGTH_SHORT)
                            .show();

                }

            }
        });

        try {

            if (getArguments().getInt(ID) == 0) {
                isNewCourse = true;

                course = new CourseRO("",
                        DateUtils.convertStringToTimestamp("01-Jan-1990"),
                        DateUtils.convertStringToTimestamp("01-Jan-1990"),
                        Constants.PLAN_STATUS,
                        "",
                        "",
                        DateUtils.convertStringToTimestamp("01-Jan-1990"),
                        DateUtils.convertStringToTimestamp("01-Jan-1990")
                );
                courseDAO.add(course);
                course = courseDAO.get(courseDAO.getLastId());
            } else {
                isNewCourse = false;

                course = courseDAO.get((getArguments().getInt(ID)));

                titleEditText.setText(course.getTitle());
                start = course.getStart();
                startDate.setText(DateUtils.convertTimestampToString(start));
                end = course.getEnd();
                endDate.setText(DateUtils.convertTimestampToString(end));
                endAlert = course.getEndAlert();
                endAlertDate.setText(DateUtils.convertTimestampToString(endAlert));
                startAlert = course.getStartAlert();
                startAlertDate.setText(DateUtils.convertTimestampToString(startAlert));
                mentorEditText.setText(course.getMentor());
                if (course.getNotes() != null) {
                    notesEditText.setText(course.getNotes());
                }

                setSpinner(statusSpinner);
            }

            setUpRecycler(v);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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

    private void setSpinner(Spinner statusSpinner) {
        String status = course.getStatus();
        int index;
        switch (status) {
            case Constants.IN_PROGRESS_STATUS:
                index = 1;
                break;
            case Constants.COMPLETE_STATUS:
                index = 2;
                break;
            case Constants.DROPPED_STATUS:
                index = 3;
                break;
            default:
                index = 0;
        }
        statusSpinner.setSelection(index);
    }

    private void setUpRecycler(View v) throws ParseException {
        RecyclerView associateNewCoursesRecycler =
                v.findViewById(R.id.associateNewCoursesRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        assessmentDAO = new AssessmentDAO(getContext());
        AssociateAssessmentAdapter adapter = new AssociateAssessmentAdapter(assessmentDAO.getAll(),
                course,
                associateNewCoursesRecycler,
                getContext());

        associateNewCoursesRecycler.setLayoutManager(manager);
        associateNewCoursesRecycler.setAdapter(adapter);
    }

    public void onCourseSave(int id) {
        if (mListener != null) {
            mListener.onCourseFinalized(id);
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
        void onCourseFinalized(int courseId);

        void goToHomePage();
    }
}
