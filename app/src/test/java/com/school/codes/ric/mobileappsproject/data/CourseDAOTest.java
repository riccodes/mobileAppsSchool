package com.school.codes.ric.mobileappsproject.data;

import com.school.codes.ric.mobileappsproject.BuildConfig;
import com.school.codes.ric.mobileappsproject.resource.CourseRO;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, packageName = "com.school.codes.ric.mobileappsproject")
public class CourseDAOTest {

    private CourseDAO courseDAO;
    private String title;
    private Timestamp start;
    private Timestamp end;
    private String status;
    private String mentor;
    private String notes;
    private Timestamp startAlert;
    private Timestamp endAlert;

    @Before
    public void before() throws ParseException {
        courseDAO = new CourseDAO(RuntimeEnvironment.application);
        courseDAO.clearDB(COURSE_TABLE_NAME);

        title = "test course";
        start = DateUtils.convertStringToTimestamp("01-jul-2018");
        end = DateUtils.convertStringToTimestamp("01-dec-2018");
        status = "IN PROGRESS";
        mentor = "My mentor (123) 123-4567 123 fake st";
        notes = "some test notes";
        startAlert = DateUtils.convertStringToTimestamp("30-jun-2018");
        endAlert = DateUtils.convertStringToTimestamp("30-nov-2018");

        courseDAO.add(new CourseRO(
                title, start, end, status, mentor, notes, startAlert, endAlert));
    }

    @After
    public void after(){
        courseDAO.clearDB(COURSE_TABLE_NAME);
    }

    @Test
    public void tesAddAndGetCourse() throws ParseException {

        CourseRO course = courseDAO.get(1);

        assertEquals("Title was incorrect ", title, course.getTitle());
        assertEquals("Start date was incorrect ",
                DateUtils.convertTimestampToString(start),
                DateUtils.convertTimestampToString(course.getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(end),
                DateUtils.convertTimestampToString(course.getEnd()));
        assertEquals("Status was incorrect ", status, course.getStatus());
        assertEquals("Mentor was incorrect ", mentor, course.getMentor());
        assertEquals("Notes was incorrect ", notes, course.getNotes());
        assertEquals("Start Alert date was incorrect ",
                DateUtils.convertTimestampToString(startAlert),
                DateUtils.convertTimestampToString(course.getStartAlert()));
        assertEquals("End Alert date was incorrect ",
                DateUtils.convertTimestampToString(endAlert),
                DateUtils.convertTimestampToString(course.getEndAlert()));
    }

    @Test
    public void tesGetAllCourses() throws ParseException {

        CourseRO course = new CourseRO("course 2",
                DateUtils.convertStringToTimestamp("12-jun-2018"),
                DateUtils.convertStringToTimestamp("12-oct-2018"),
                "COMPLETED",
                "A Mock Mentor (123)987-6543",
                "These are not real notes",
                DateUtils.convertStringToTimestamp("11-jun-2018"),
                DateUtils.convertStringToTimestamp("11-oct-2018"));
        courseDAO.add(course);

        List<CourseRO> courses = courseDAO.getAll();

        assertEquals("list size was wrong", 2, courses.size());

        assertEquals("Title was incorrect for course 1 ", title, courses.get(0).getTitle());
        assertEquals("Start date was incorrect ",
                        DateUtils.convertTimestampToString(start),
                        DateUtils.convertTimestampToString(courses.get(0).getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(end),
                DateUtils.convertTimestampToString(courses.get(0).getEnd()));
        assertEquals("Status was incorrect for course 1 ",
                status, courses.get(0).getStatus());
        assertEquals("Mentor was incorrect for course 1 ",
                mentor, courses.get(0).getMentor());
        assertEquals("Notes was incorrect for course 1 ",
                notes, courses.get(0).getNotes());
        assertEquals("Start Alert date was incorrect ",
                DateUtils.convertTimestampToString(startAlert),
                DateUtils.convertTimestampToString(courses.get(0).getStartAlert()));
        assertEquals("End Alert date was incorrect ",
                DateUtils.convertTimestampToString(endAlert),
                DateUtils.convertTimestampToString(courses.get(0).getEndAlert()));

        assertEquals("Title was incorrect for course 2", course.getTitle(),
                courses.get(1).getTitle());
        assertEquals("Start date was incorrect for course 2 ",
                DateUtils.convertTimestampToString(course.getStart()),
                DateUtils.convertTimestampToString(courses.get(1).getStart()));
        assertEquals("End date was incorrect for course 2",
                DateUtils.convertTimestampToString(course.getEnd()),
                DateUtils.convertTimestampToString(courses.get(1).getEnd()));
        assertEquals("Status was incorrect for course 2", course.getStatus(),
                courses.get(1).getStatus());
        assertEquals("Mentor was incorrect for course 2", course.getMentor(),
                courses.get(1).getMentor());
        assertEquals("Notes was incorrect for course 2", course.getNotes(),
                courses.get(1).getNotes());
        assertEquals("Start Alert date was incorrect for course 2",
                DateUtils.convertTimestampToString(course.getStartAlert()),
                DateUtils.convertTimestampToString(courses.get(1).getStartAlert()));
        assertEquals("End Alert date was incorrect for course2",
                DateUtils.convertTimestampToString(course.getEndAlert()),
                DateUtils.convertTimestampToString(courses.get(1).getEndAlert()));
    }

    @Test
    public void testDeleteCourse() throws ParseException {
        courseDAO.delete(1);
        CourseRO course = courseDAO.get(1);
        assertEquals("course was found", null, course);
    }

    @Test
    public void testUpdateCourse() throws ParseException {

        CourseRO course = courseDAO.get(1);
        assertEquals("Title was different", title, course.getTitle());

        CourseRO updatedCourse = new CourseRO("updated title",
                DateUtils.convertStringToTimestamp("01-aug-2018"),
                DateUtils.convertStringToTimestamp("01-jan-2019"),
                "updated status","updated mentor","updated notes",
                DateUtils.convertStringToTimestamp("31-jul-2018"),
                DateUtils.convertStringToTimestamp("31-dec-2018"));
        updatedCourse.setId(1);
        courseDAO.update(updatedCourse);

        course = courseDAO.get(1);
        assertEquals("Title was incorrect ",
                updatedCourse.getTitle(),
                course.getTitle());
        assertEquals("Start date was incorrect ",
                DateUtils.convertTimestampToString(updatedCourse.getStart()),
                DateUtils.convertTimestampToString(course.getStart()));
        assertEquals("End date was incorrect ",
                DateUtils.convertTimestampToString(updatedCourse.getEnd()),
                DateUtils.convertTimestampToString(updatedCourse.getEnd()));
        assertEquals("Status was incorrect ",
                updatedCourse.getStatus(),
                course.getStatus());
        assertEquals("Mentor was incorrect ",
                updatedCourse.getMentor(),
                course.getMentor());
        assertEquals("Notes were incorrect ",
                updatedCourse.getNotes(),
                course.getNotes());
        assertEquals("Start date Alert was incorrect ",
                DateUtils.convertTimestampToString(updatedCourse.getStartAlert()),
                DateUtils.convertTimestampToString(course.getStartAlert()));
        assertEquals("End date Alert was incorrect ",
                DateUtils.convertTimestampToString(updatedCourse.getEndAlert()),
                DateUtils.convertTimestampToString(updatedCourse.getEndAlert()));

    }
}