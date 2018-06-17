package com.school.codes.ric.mobileappsproject;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.school.codes.ric.mobileappsproject.data.TermDAO;
import com.school.codes.ric.mobileappsproject.resource.TermRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBTest {

    private TermDAO termDAO;
    private Context context;
    private String title;
    private Timestamp start;
    private Timestamp end;

    @Before
    public void before() throws ParseException {
        context = InstrumentationRegistry.getTargetContext();
        termDAO = new TermDAO(context);
        termDAO.clearDB();
        title = "test term";
        start = DateUtils.convertStringToTimestamp("01-jul-2018");
        end = DateUtils.convertStringToTimestamp("01-dec-2018");

        termDAO.add(new TermRO(title, start, end));
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

}
