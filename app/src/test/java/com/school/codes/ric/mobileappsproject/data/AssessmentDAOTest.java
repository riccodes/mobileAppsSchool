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
import static org.junit.Assert.assertEquals;

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
        assessmentDAO.delete(1);
        AssessmentRO assessment = assessmentDAO.get(1);
        assertEquals("assessment was found", null, assessment);
    }

    @Test
    public void testUpdateAssessment() throws ParseException {

        AssessmentRO assessment = assessmentDAO.get(1);
        assertEquals("Title was different", title, assessment.getTitle());

        AssessmentRO assessmentUpdate = new AssessmentRO("updated title",
                AssessmentRO.AssessmentType.OBJECTIVE,
                DateUtils.convertStringToTimestamp("31-jul-2018"));
        assessmentUpdate.setId(1);
        assessmentDAO.update(assessmentUpdate);

        assessment = assessmentDAO.get(1);
        assertEquals("Title was incorrect ",
                assessmentUpdate.getTitle(),
                assessment.getTitle());
        assertEquals("Type was incorrect ", assessmentUpdate.getType(),
                assessment.getType());
        assertEquals("Goal Alert was incorrect ",
                DateUtils.convertTimestampToString(assessmentUpdate.getGoalDate()),
                DateUtils.convertTimestampToString(assessment.getGoalDate()));

    }

    @Test
    public void testGetAllAssociatedAssessments() throws ParseException {

        AssessmentRO a3 = new AssessmentRO("a3", AssessmentRO.AssessmentType.PERFORMANCE,
                DateUtils.convertStringToTimestamp("10-oct-2016"));
        assessmentDAO.add(a3);
        a3.setCourseId(101);
        assessmentDAO.update(a3);

        AssessmentRO a2 = new AssessmentRO("a2", AssessmentRO.AssessmentType.PERFORMANCE,
                DateUtils.convertStringToTimestamp("10-oct-2016"));
        assessmentDAO.add(a2);

        CourseRO course = new CourseRO();
        course.setId(100);

        AssessmentRO a1 = assessmentDAO.get(1);
        a1.setCourseId(course.getId());
        assessmentDAO.update(a1);

        a2 = assessmentDAO.get(2);
        a2.setCourseId(course.getId());
        assessmentDAO.update(a2);

        List<AssessmentRO> assessments =
                assessmentDAO.getAllAssessmentsForCourse(course.getId());
        assertEquals("size was incorrect", 2, assessments.size());
        assertEquals("title was incorrect for item one", a1.getTitle(),
                assessments.get(0).getTitle());
        assertEquals("title was incorrect for item two", a2.getTitle(),
                assessments.get(1).getTitle());

    }
}