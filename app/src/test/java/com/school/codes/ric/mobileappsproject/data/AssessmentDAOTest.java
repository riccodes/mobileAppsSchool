package com.school.codes.ric.mobileappsproject.data;

import com.school.codes.ric.mobileappsproject.BuildConfig;
import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
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
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, packageName = "com.school.codes.ric.mobileappsproject")
public class AssessmentDAOTest {

    private AssessmentDAO assessmentDAO;
    private String title;
    private Timestamp goal;
    private AssessmentRO.AssessmentType type;

    @Before
    public void before() throws ParseException {
        assessmentDAO = new AssessmentDAO(RuntimeEnvironment.application);
        assessmentDAO.clearDB(ASSESSMENT_TABLE_NAME);

        title = "test assessment";
        goal = DateUtils.convertStringToTimestamp("01-jul-2018");
        type = AssessmentRO.AssessmentType.PERFORMANCE;

        assessmentDAO.add(new AssessmentRO(title, type, goal));
    }

    @After
    public void after(){
        assessmentDAO.clearDB(ASSESSMENT_TABLE_NAME);
    }

    @Test
    public void tesAddAndGetAssessment() throws ParseException {

        AssessmentRO assessment = assessmentDAO.get(1);

        assertEquals("Title was incorrect ", title, assessment.getTitle());
        assertEquals("Goal date was incorrect ",
                DateUtils.convertTimestampToString(goal),
                DateUtils.convertTimestampToString(assessment.getGoalDate()));
        assertEquals("Type was incorrect ", type, assessment.getType());
    }

    @Test
    public void tesGetAllAssessment() throws ParseException {

        AssessmentRO assessment = new AssessmentRO("assessment updated",
                AssessmentRO.AssessmentType.OBJECTIVE,
                DateUtils.convertStringToTimestamp("10-jun-2018"));
        assessmentDAO.add(assessment);

        List<AssessmentRO> assessments = assessmentDAO.getAll();

        assertEquals("list size was wrong", 2, assessments.size());

        assertEquals("Title was incorrect for assessment 1 ",
                title, assessments.get(0).getTitle());
        assertEquals("Goal date was incorrect ",
                        DateUtils.convertTimestampToString(goal),
                        DateUtils.convertTimestampToString(assessments.get(0).getGoalDate()));
        assertEquals("Type was incorrect for assessment 1 ",
                type, assessments.get(0).getType());

        assertEquals("Title was incorrect for assessment 2",
                assessment.getTitle(), assessments.get(1).getTitle());
        assertEquals("Goal date was incorrect for assessment 2",
                DateUtils.convertTimestampToString(assessment.getGoalDate()),
                DateUtils.convertTimestampToString(assessments.get(1).getGoalDate()));
        assertEquals("Type was incorrect for assessment 1 ",
                assessment.getType(), assessments.get(1).getType());
    }

    @Test
    public void testDeleteAssessment() throws ParseException {
//        assessmentDAO.delete(1);
//        CourseRO course = assessmentDAO.get(1);
//        assertEquals("course was found", null, course);
        fail("not implemented");
    }

    @Test
    public void testUpdateAssessment() throws ParseException {

//        CourseRO course = assessmentDAO.get(1);
//        assertEquals("Title was different", title, course.getTitle());
//
//        CourseRO updatedCourse = new CourseRO("updated title",
//                DateUtils.convertStringToTimestamp("01-aug-2018"),
//                DateUtils.convertStringToTimestamp("01-jan-2019"),
//                "updated status","updated mentor","updated notes",
//                DateUtils.convertStringToTimestamp("31-jul-2018"),
//                DateUtils.convertStringToTimestamp("31-dec-2018"));
//        updatedCourse.setId(1);
//        assessmentDAO.update(updatedCourse);
//
//        course = assessmentDAO.get(1);
//        assertEquals("Title was incorrect ",
//                updatedCourse.getTitle(),
//                course.getTitle());
//        assertEquals("Start date was incorrect ",
//                DateUtils.convertTimestampToString(updatedCourse.getStart()),
//                DateUtils.convertTimestampToString(course.getStart()));
//        assertEquals("End date was incorrect ",
//                DateUtils.convertTimestampToString(updatedCourse.getEnd()),
//                DateUtils.convertTimestampToString(updatedCourse.getEnd()));
//        assertEquals("Status was incorrect ",
//                updatedCourse.getStatus(),
//                course.getStatus());
//        assertEquals("Mentor was incorrect ",
//                updatedCourse.getMentor(),
//                course.getMentor());
//        assertEquals("Notes were incorrect ",
//                updatedCourse.getNotes(),
//                course.getNotes());
//        assertEquals("Start date Alert was incorrect ",
//                DateUtils.convertTimestampToString(updatedCourse.getStartAlert()),
//                DateUtils.convertTimestampToString(course.getStartAlert()));
//        assertEquals("End date Alert was incorrect ",
//                DateUtils.convertTimestampToString(updatedCourse.getEndAlert()),
//                DateUtils.convertTimestampToString(updatedCourse.getEndAlert()));
        fail("not implemented");

    }
}