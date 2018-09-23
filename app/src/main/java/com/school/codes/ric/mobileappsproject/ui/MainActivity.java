package com.school.codes.ric.mobileappsproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.AssessmentDAO;
import com.school.codes.ric.mobileappsproject.data.CourseDAO;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.PLAN_STATUS;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddTermFragment.OnFragmentInteractionListener,
        TermDetailFragment.OnFragmentInteractionListener,
        AddCourseFragment.OnFragmentInteractionListener,
        CourseGridFragment.OnCourseInteractionListener,
        CourseDetailFragment.OnFragmentInteractionListener,
        AllCoursesGridFragment.OnCourseInteractionListener,
        AssessmentGridFragment.OnAssessmentInteractionListener,
        AddAssessmentFragment.OnAssessmentInteractionListener,
        AssessmentDetailFragment.OnAssessmentInteractionListener,
        AllAssessmentGridFragment.OnAssessmentInteractionListener {

    private static final String COURSES_INTENT = "COURSES_INTENT";
    private static final String ASSESSMENTS_INTENT = "ASSESSMENTS_INTENT";
    private static final String TAG = MainActivity.class.getSimpleName();

    private FragmentManager manager;
    private NavigationView navigationView;
    private Menu navMenu;
    private TermDAO termDAO;

    //todo: dynamic button on associate
    //todo: handle back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();

        loadTestData();

        fabItUp();

        displayHome();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navMenu = navigationView.getMenu();

        populateDrawerMenu();

    }

    private void displayHome() {
        clearAllFragments();
        HomeFragment home = HomeFragment.newInstance();
        manager.beginTransaction()
                .replace(R.id.detailFrameLayout, home)
                .addToBackStack(null)
                .commit();
    }

    private void populateDrawerMenu() {

        List<TermRO> terms = null;
        try {
            terms = termDAO.getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        navMenu.clear();
        Menu termMenu = navMenu.addSubMenu("All Terms");
        MenuItem courseMenu = navMenu.add("All Courses");
        MenuItem assessmentMenu = navMenu.add("All Assessments");

        assert terms != null;
        for (TermRO t : terms) {
            termMenu.add(t.getTitle()).setIntent(new Intent(t.getId() + ""));
        }

        courseMenu.setIntent(new Intent(COURSES_INTENT));
        assessmentMenu.setIntent(new Intent(ASSESSMENTS_INTENT));

        navigationView.invalidate();
    }

    private void fabItUp() {
        final FloatingActionsMenu fabMenu = findViewById(R.id.fab_menu);

        final FloatingActionButton addTerm = new FloatingActionButton(this);
        addTerm.setTitle(getString(R.string.add_term));
        addTerm.setIconDrawable(getResources()
                .getDrawable(android.R.drawable.ic_menu_day,
                getTheme()));
        addTerm.setSize(FloatingActionButton.SIZE_MINI);
        addTerm.setColorNormal(ContextCompat.getColor(getApplicationContext(),
                R.color.colorAccent));
        addTerm.setColorPressed((ContextCompat.getColor(getApplicationContext(),
                R.color.textPrimary)));
        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFragments();
                addTerm(0);
                fabMenu.collapse();
            }
        });

        final FloatingActionButton addCourse = new FloatingActionButton(this);
        addCourse.setTitle(getString(R.string.add_course));
        addCourse.setSize(FloatingActionButton.SIZE_MINI);
        addCourse.setColorNormal(ContextCompat.getColor(getApplicationContext(),
                R.color.colorAccent));
        addCourse.setColorPressed((ContextCompat.getColor(getApplicationContext(),
                R.color.textPrimary)));
        addCourse.setIconDrawable(getResources()
                .getDrawable(android.R.drawable.ic_menu_sort_by_size,
                getTheme()));
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFragments();
                addCourse(0);
                fabMenu.collapse();
            }
        });

        final FloatingActionButton addAssessment = new FloatingActionButton(this);
        addAssessment.setTitle(getString(R.string.add_assessment));
        addAssessment.setSize(FloatingActionButton.SIZE_MINI);
        addAssessment.setColorNormal(ContextCompat.getColor(getApplicationContext(),
                R.color.colorAccent));
        addAssessment.setColorPressed((ContextCompat.getColor(getApplicationContext(),
                R.color.textPrimary)));
        addAssessment.setIconDrawable(getResources()
                .getDrawable(android.R.drawable.ic_menu_edit,
                getTheme()));
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFragments();
                addAssessment(0);
                fabMenu.collapse();
            }
        });

        fabMenu.addButton(addTerm);
        fabMenu.addButton(addCourse);
        fabMenu.addButton(addAssessment);
    }

    private void addAssessment(int id) {
        clearAllFragments();
        AddAssessmentFragment addFrag = AddAssessmentFragment.newInstance(id);
        manager.beginTransaction()
                .replace(R.id.mainLayout, addFrag).commit();
    }

    private void addTerm(int id) {
        clearAllFragments();
        AddTermFragment addFrag = AddTermFragment.newInstance(id);
        manager.beginTransaction()
                .replace(R.id.mainLayout, addFrag)
                .addToBackStack(null)
                .commit();
    }

    private void addCourse(int id) {
        clearAllFragments();
        AddCourseFragment addFrag = AddCourseFragment.newInstance(id);
        manager.beginTransaction()
                .replace(R.id.mainLayout, addFrag)
                .addToBackStack(null)
                .commit();
    }

    private void clearAllFragments() {
        for (Fragment f : manager.getFragments()) {
            manager.beginTransaction().remove(f).commit();
        }
    }

    private void displayTermView(int id) {
        clearAllFragments();

        CourseGridFragment frag = CourseGridFragment.newInstance(id);
        TermDetailFragment detailFrag = TermDetailFragment.newInstance(id);

        manager.beginTransaction()
                .replace(R.id.detailFrameLayout, detailFrag)
                .replace(R.id.contentFrameLayout, frag)
                .addToBackStack(null)
                .commit();
    }

    private void displayCourseView(int id) {
        clearAllFragments();

        AssessmentGridFragment frag = AssessmentGridFragment.newInstance(id);
        CourseDetailFragment detailFrag = CourseDetailFragment.newInstance(id);

        manager.beginTransaction()
                .replace(R.id.detailFrameLayout, detailFrag)
                .replace(R.id.contentFrameLayout, frag)
                .addToBackStack(null)
                .commit();
    }

    private void displayAllAssessments() {
        clearAllFragments();

        AllAssessmentGridFragment frag = AllAssessmentGridFragment.newInstance();

        manager.beginTransaction()
                .replace(R.id.contentFrameLayout, frag)
                .addToBackStack(null)
                .commit();
    }

    private void displayAllCourses() {
        clearAllFragments();

        AllCoursesGridFragment frag = AllCoursesGridFragment.newInstance();

        manager.beginTransaction()
                .replace(R.id.contentFrameLayout, frag)
                .addToBackStack(null)
                .commit();
    }

    private void displayAssessmentView(int id) {
        clearAllFragments();

        AssessmentDetailFragment detailFrag = AssessmentDetailFragment.newInstance(id);

        manager.beginTransaction()
                .replace(R.id.detailFrameLayout, detailFrag)
                .addToBackStack(null)
                .commit();
    }

    private void loadTestData() {

        //Load test Assessments
        Timestamp a1GoalDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        a1GoalDate.setDate(30);
        a1GoalDate.setMonth(0);

        AssessmentRO a1 =
                new AssessmentRO("Objective 1", AssessmentRO.AssessmentType.OBJECTIVE, a1GoalDate);

        Timestamp a2GoalDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        a2GoalDate.setDate(20);
        a2GoalDate.setMonth(1);

        AssessmentRO a2 =
                new AssessmentRO("Performance 1", AssessmentRO.AssessmentType.PERFORMANCE, a2GoalDate);

        Timestamp a3GoalDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        a3GoalDate.setDate(20);
        a3GoalDate.setMonth(2);

        AssessmentRO a3 =
                new AssessmentRO("Objective 2", AssessmentRO.AssessmentType.OBJECTIVE, a3GoalDate);

        Timestamp a4GoalDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        a4GoalDate.setDate(22);
        a4GoalDate.setMonth(3);

        AssessmentRO a4 =
                new AssessmentRO("Performance 2", AssessmentRO.AssessmentType.PERFORMANCE, a4GoalDate);

        //Load test Courses
        Timestamp c1StartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c1StartDate.setDate(3);
        c1StartDate.setMonth(0);

        Timestamp c1EndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c1EndDate.setDate(3);
        c1EndDate.setMonth(1);

        CourseRO c1 =
                new CourseRO("Math", c1StartDate, c1EndDate,
                        PLAN_STATUS, "My mentor", "These are test notes",
                        c1StartDate, c1EndDate);

        Timestamp c2StartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c2StartDate.setDate(4);
        c2StartDate.setMonth(1);

        Timestamp c2EndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c2EndDate.setDate(3);
        c2EndDate.setMonth(6);

        CourseRO c2 =
                new CourseRO("English", c2StartDate, c2EndDate,
                        PLAN_STATUS, "My mentor 2", "These are test notes for this course",
                        c2StartDate, c2EndDate);

        Timestamp c3StartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c3StartDate.setDate(1);
        c3StartDate.setMonth(7);

        Timestamp c3EndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c3EndDate.setDate(1);
        c3EndDate.setMonth(9);

        CourseRO c3 =
                new CourseRO("Programming 101", c3StartDate, c3EndDate,
                        PLAN_STATUS, "My mentor 3", "These are test notes for this course",
                        c2StartDate, c3EndDate);

        Timestamp c4StartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c4StartDate.setDate(2);
        c4StartDate.setMonth(9);

        Timestamp c4EndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        c4EndDate.setDate(13);
        c4EndDate.setMonth(11);

        CourseRO c4 =
                new CourseRO("Java Fundamentals", c4StartDate, c4EndDate,
                        PLAN_STATUS, "My mentor 4", "These are test notes for this course",
                        c4StartDate, c4EndDate);

        //Load test Terms
        Timestamp springStartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        springStartDate.setDate(3);
        springStartDate.setMonth(0);

        Timestamp springEndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        springEndDate.setDate(6);
        springEndDate.setMonth(4);

        Timestamp fallStartDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        fallStartDate.setDate(1);
        fallStartDate.setMonth(7);

        Timestamp fallEndDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        fallEndDate.setDate(13);
        fallEndDate.setMonth(11);

        TermRO spring = new TermRO("Spring Term", springStartDate, springEndDate);
        TermRO fall = new TermRO("Fall Term", fallStartDate, fallEndDate);

        // Add data to DB
        termDAO = new TermDAO(getApplicationContext());
        termDAO.clearDB(ASSESSMENT_TABLE_NAME);
        termDAO.clearDB(COURSE_TABLE_NAME);
        termDAO.clearDB(TERM_TABLE_NAME);

        CourseDAO courseDAO = new CourseDAO(getApplicationContext());
        AssessmentDAO assessmentDAO = new AssessmentDAO(getApplicationContext());

        try {

            termDAO.add(spring);
            spring = termDAO.get(termDAO.getLastId());
            termDAO.add(fall);
            fall = termDAO.get(termDAO.getLastId());

            courseDAO.add(c1);
            c1 = courseDAO.get(courseDAO.getLastId());
            c1.setTermId(spring.getId());
            courseDAO.update(c1);

            courseDAO.add(c2);
            c2 = courseDAO.get(courseDAO.getLastId());
            c2.setTermId(spring.getId());
            courseDAO.update(c2);

            courseDAO.add(c3);
            c3 = courseDAO.get(courseDAO.getLastId());
            c3.setTermId(fall.getId());
            courseDAO.update(c3);

            courseDAO.add(c4);
            c4 = courseDAO.get(courseDAO.getLastId());
            c4.setTermId(fall.getId());
            courseDAO.update(c4);

            assessmentDAO.add(a1);
            a1 = assessmentDAO.get(assessmentDAO.getLastId());
            a1.setCourseId(c1.getId());
            assessmentDAO.update(a1);

            assessmentDAO.add(a2);
            a2 = assessmentDAO.get(assessmentDAO.getLastId());
            a2.setCourseId(c1.getId());
            assessmentDAO.update(a2);

            assessmentDAO.add(a3);
            a3 = assessmentDAO.get(assessmentDAO.getLastId());
            a3.setCourseId(c2.getId());
            assessmentDAO.update(a3);

            assessmentDAO.add(a4);
            a4 = assessmentDAO.get(assessmentDAO.getLastId());
            a4.setCourseId(c2.getId());
            assessmentDAO.update(a4);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (Objects.requireNonNull(item).getIntent().getAction().equalsIgnoreCase(COURSES_INTENT)) {
            displayAllCourses();
        } else if (Objects.requireNonNull(item).getIntent().getAction().equalsIgnoreCase(ASSESSMENTS_INTENT)) {
            displayAllAssessments();
        } else {
            int currentTermId =
                    Integer.parseInt(Objects.requireNonNull(item).getIntent().getAction());

            displayTermView(currentTermId);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTermFinalized(int termId) {
        displayTermView(termId);
        populateDrawerMenu();
    }

    @Override
    public void onAssessmentFinalized(int assessmentId) {
        displayAssessmentView(assessmentId);
        populateDrawerMenu();
    }

    @Override
    public void onCourseFinalized(int courseId) {
        displayCourseView(courseId);
        populateDrawerMenu();
    }

    @Override
    public void onCourseClicked(int id) {
        displayCourseView(id);
    }

    @Override
    public void onAssessmentClicked(int id) {
        displayAssessmentView(id);
    }

    @Override
    public void goToHomePage() {
        displayHome();
        populateDrawerMenu();
    }

    @Override
    public void editAssessment(int id) {
        addAssessment(id);
    }

    @Override
    public void editCourse(int id) {
        addCourse(id);
    }

    @Override
    public void editTerm(int id) {
        addTerm(id);
    }

}
