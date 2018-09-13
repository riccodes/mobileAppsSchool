package com.school.codes.ric.mobileappsproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

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

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String COURSES_INTENT = "COURSES_INTENT";
    private static final String ASSESSMENTS_INTENT = "ASSESSMENTS_INTENT";

    private FragmentManager manager;
    private NavigationView navigationView;
    private Menu navMenu;
    private DrawerLayout drawer;
    private FloatingActionsMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();

        fabItUp();

        displayHome();

        drawer = findViewById(R.id.drawer_layout);
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
        replaceFragments(home, null);
    }

    private void populateDrawerMenu() {

        List<TermRO> terms = null;
        try {
            TermDAO termDAO = new TermDAO(getApplicationContext());
            terms = termDAO.getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        navMenu.clear();
        Menu termMenu = navMenu.addSubMenu("Terms");
        MenuItem courseMenu = navMenu.add("Courses");
        MenuItem assessmentMenu = navMenu.add("Assessments");

        assert terms != null;
        for (TermRO t : terms) {
            termMenu.add(t.getTitle()).setIntent(new Intent(t.getId() + ""));
        }

        courseMenu.setIntent(new Intent(COURSES_INTENT));
        assessmentMenu.setIntent(new Intent(ASSESSMENTS_INTENT));

        navigationView.invalidate();
    }

    private void fabItUp() {
        fabMenu = findViewById(R.id.fab_menu);

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

    private void clearAllFragments() {
        for (Fragment f : manager.getFragments()) {
            manager.beginTransaction().remove(f).commit();
        }
    }

    private void replaceFragments(Fragment f1, Fragment f2) {

        FragmentTransaction transaction = manager.beginTransaction();

        if (f1 != null) {
            transaction.replace(R.id.detailFrameLayout, f1);
        }

        if (f2 != null) {
            transaction.replace(R.id.contentFrameLayout, f2);
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void addAssessment(int id) {
        clearAllFragments();
        AddAssessmentFragment addFrag = AddAssessmentFragment.newInstance(id);
        replaceFragments(addFrag, null);
    }

    private void addTerm(int id) {
        clearAllFragments();
        AddTermFragment addFrag = AddTermFragment.newInstance(id);
        replaceFragments(addFrag, null);
    }

    private void addCourse(int id) {
        clearAllFragments();
        AddCourseFragment addFrag = AddCourseFragment.newInstance(id);
        replaceFragments(addFrag, null);
    }

    private void displayTermView(int id) {
        clearAllFragments();

        CourseGridFragment frag = CourseGridFragment.newInstance(id);
        TermDetailFragment detailFrag = TermDetailFragment.newInstance(id);

        replaceFragments(detailFrag, frag);
    }

    private void displayCourseView(int id) {
        clearAllFragments();

        AssessmentGridFragment frag = AssessmentGridFragment.newInstance(id);
        CourseDetailFragment detailFrag = CourseDetailFragment.newInstance(id);

        replaceFragments(detailFrag, frag);
    }

    private void displayAllAssessments() {
        clearAllFragments();

        AllAssessmentGridFragment frag = AllAssessmentGridFragment.newInstance();

        replaceFragments(frag, null);
    }

    private void displayAllCourses() {
        clearAllFragments();

        AllCoursesGridFragment frag = AllCoursesGridFragment.newInstance();

        replaceFragments(frag, null);
    }

    private void displayAssessmentView(int id) {
        clearAllFragments();

        AssessmentDetailFragment frag = AssessmentDetailFragment.newInstance(id);

        replaceFragments(frag, null);
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawer.openDrawer(Gravity.START);
        fabMenu.expand();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(Gravity.START);

                Handler h2 = new Handler();
                h2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fabMenu.collapse();
                    }
                }, 1000);

            }
        }, 2000);
    }
}
