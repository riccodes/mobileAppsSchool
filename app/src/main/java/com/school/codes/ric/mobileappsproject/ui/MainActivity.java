package com.school.codes.ric.mobileappsproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.school.codes.ric.mobileappsproject.R;
import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;

import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AddTermFragment.OnFragmentInteractionListener {

    private FragmentManager manager;
    private NavigationView navigationView;
    private Menu navMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();

        fabItUp();

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

    private void populateDrawerMenu() {

        List<TermRO> terms = null;
        try {
            TermDAO termDAO = new TermDAO(getApplicationContext());
            terms = termDAO.getAll();
        } catch (ParseException e) {
            e.printStackTrace(); //todo: handle this properly
        }

        navMenu.clear();
        Menu termMenu = navMenu.addSubMenu("Terms");
        for (TermRO t : terms) {
            termMenu.add(t.getTitle()).setIntent(new Intent(t.getId() + ""));
        }

        navigationView.invalidate();
    }

    private void fabItUp() {
        final FloatingActionsMenu fabMenu = findViewById(R.id.fab_menu);

        final FloatingActionButton addTerm = new FloatingActionButton(this);
        addTerm.setTitle("Add Term");
        addTerm.setSize(FloatingActionButton.SIZE_MINI);
        addTerm.setIconDrawable(getResources().getDrawable(android.R.drawable.ic_menu_day,
                getTheme()));
        addTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFragments();
                AddTermFragment addFrag = AddTermFragment.newInstance();
                manager.beginTransaction()
                        .replace(R.id.mainLayout, addFrag).commit();
                fabMenu.collapse();
            }
        });

        final FloatingActionButton addCourse = new FloatingActionButton(this);
        addCourse.setTitle("Add Course");
        addCourse.setSize(FloatingActionButton.SIZE_MINI);
        addCourse.setIconDrawable(getResources().getDrawable(android.R.drawable.ic_menu_sort_by_size,
                getTheme()));
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), addCourse.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        final FloatingActionButton addAssessment = new FloatingActionButton(this);
        addAssessment.setTitle("Add Assessment");
        addAssessment.setSize(FloatingActionButton.SIZE_MINI);
        addAssessment.setIconDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit,
                getTheme()));
        addAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), addAssessment.getTitle(), Toast.LENGTH_SHORT).show();
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

    private void displayTermView(int id) {
        clearAllFragments();

        GridFragment frag = GridFragment.newInstance(id);
        DetailFragment detailFrag = DetailFragment.newInstance(id);

        manager.beginTransaction()
                .replace(R.id.detailFrameLayout, detailFrag)
                .replace(R.id.contentFrameLayout, frag)
                .commit();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int currentTermId = Integer.parseInt(item.getIntent().getAction());

        displayTermView(currentTermId);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTermFinalized(int termId) {
        displayTermView(termId);
        populateDrawerMenu();
    }
}
