package com.school.codes.ric.mobileappsproject.data;

import android.database.sqlite.SQLiteException;

import com.school.codes.ric.mobileappsproject.BuildConfig;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_NAME;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, packageName = "com.school.codes.ric.mobileappsproject")
public class TermDAOTest {

    private TermDAO termDAO;
    private TermRO term;
    private String title;
    private Timestamp start;
    private Timestamp end;

    @Before
    public void before() throws ParseException {
        termDAO = new TermDAO(RuntimeEnvironment.application);
        termDAO.clearDB(TERM_TABLE_NAME);
        title = "test term";
        start = DateUtils.convertStringToTimestamp("01-jul-2018");
        end = DateUtils.convertStringToTimestamp("01-dec-2018");

        term = new TermRO(title, start, end);
        termDAO.add(term);
    }

    @After
    public void after(){
        termDAO.clearDB(TERM_TABLE_NAME);
    }

    @Test
    public void tesAddAndGetTerm() throws ParseException {

        TermRO term = termDAO.get(1);

        assertEquals("Title was incorrect ", title, term.getTitle());
        assertEquals("Start date was incorrect ",
                DateUtils.convertTimestampToString(start),
                DateUtils.convertTimestampToString(term.getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(end),
                DateUtils.convertTimestampToString(term.getEnd()));
    }

    @Test
    public void tesGetAllTerms() throws ParseException {

        TermRO term = new TermRO("term 2",
                DateUtils.convertStringToTimestamp("12-jun-2018"),
                DateUtils.convertStringToTimestamp("12-oct-2018"));
        termDAO.add(term);

        List<TermRO> terms = termDAO.getAll();

        assertEquals("list size was wrong", 2, terms.size());

        assertEquals("Title was incorrect for term 1 ", title, terms.get(0).getTitle());
                assertEquals("Start date was incorrect ",
                        DateUtils.convertTimestampToString(start),
                        DateUtils.convertTimestampToString(terms.get(0).getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(end),
                DateUtils.convertTimestampToString(terms.get(0).getEnd()));

        assertEquals("Title was incorrect for term 2", term.getTitle(),
                terms.get(1).getTitle());
        assertEquals("Start date was incorrect for term 2 ",
                DateUtils.convertTimestampToString(term.getStart()),
                DateUtils.convertTimestampToString(terms.get(1).getStart()));
        assertEquals("End date was incorrect for term 2",
                DateUtils.convertTimestampToString(term.getEnd()),
                DateUtils.convertTimestampToString(terms.get(1).getEnd()));
    }

    @Test
    public void testDeleteTerm() throws ParseException {
        termDAO.delete(1);
        TermRO term = termDAO.get(1);
        assertEquals("Term was found", null, term);
    }

    @Test
    public void testUpdateTerm() throws ParseException {

        TermRO term = termDAO.get(1);
        assertEquals("Title was different", title, term.getTitle());

        TermRO updatedTerm = new TermRO("updated title",
                DateUtils.convertStringToTimestamp("01-aug-2018"),
                DateUtils.convertStringToTimestamp("01-jan-2019"));
        updatedTerm.setId(1);
        termDAO.update(updatedTerm);

        term = termDAO.get(1);
        assertEquals("Title was incorrect ", updatedTerm.getTitle(), term.getTitle());
        assertEquals("Start date was incorrect ",
                DateUtils.convertTimestampToString(updatedTerm.getStart()),
                DateUtils.convertTimestampToString(term.getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(updatedTerm.getEnd()),
                DateUtils.convertTimestampToString(updatedTerm.getEnd()));

    }

    @Test
    public void testTermShouldNotBeDeletedIfCourseAssociated() throws ParseException {
        CourseRO course = new CourseRO("test course",
                DateUtils.convertStringToTimestamp("01-jul-2018"),
                DateUtils.convertStringToTimestamp("01-dec-2018"),
                "IN PROGRESS",
                "My mentor (123) 123-4567 123 fake st",
                "some test notes",
                DateUtils.convertStringToTimestamp("30-jun-2018"),
                DateUtils.convertStringToTimestamp("30-nov-2018"));
        CourseDAO courseDAO = new CourseDAO(RuntimeEnvironment.application);
        courseDAO.clearDB(COURSE_TABLE_NAME);
        courseDAO.add(course);

        course = courseDAO.get(1);
        course.setTermId(1);
        courseDAO.update(course);

        try {
            termDAO.delete(1);
        } catch (SQLiteException e) {
            assertEquals(" class was wrong",
                    "class android.database.sqlite.SQLiteException",
                    e.getClass().toString());
        }

        courseDAO.delete(1);
    }
}