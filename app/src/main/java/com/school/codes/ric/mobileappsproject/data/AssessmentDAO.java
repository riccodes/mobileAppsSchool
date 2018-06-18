package com.school.codes.ric.mobileappsproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.school.codes.ric.mobileappsproject.resource.AssessmentRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_GOAL;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_TITLE;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_TYPE;

public class AssessmentDAO extends BaseDAO {

    private final String[] ALL_ASSESSMENT_COLUMNS = {
            ASSESSMENT_TABLE_ID,
            ASSESSMENT_TABLE_TITLE,
            ASSESSMENT_TABLE_GOAL,
            ASSESSMENT_TABLE_TYPE
    };

    public AssessmentDAO(Context context) {
        super(context);
    }

    public void add(AssessmentRO assessment) {
        open();

        ContentValues cv = new ContentValues();

        cv.put(ASSESSMENT_TABLE_TITLE, assessment.getTitle());
        cv.put(ASSESSMENT_TABLE_GOAL, DateUtils.convertTimestampToString(assessment.getGoalDate()));
        cv.put(ASSESSMENT_TABLE_TYPE, assessment.getType().toString());

        db.insert(ASSESSMENT_TABLE_NAME, null, cv);

        close();
    }

    public AssessmentRO get(int id) throws ParseException {
        open();

        String whereClause = ASSESSMENT_TABLE_ID + "=?";
        String[] args = {id+""};
        Cursor c = db.query(ASSESSMENT_TABLE_NAME, ALL_ASSESSMENT_COLUMNS, whereClause, args,
                null, null, null);

        AssessmentRO assessment = null;

        if(c.moveToFirst()){
            assessment = new AssessmentRO();
            assessment.setId(c.getInt(0));
            assessment.setTitle(c.getString(1));
            assessment.setGoalDate(DateUtils.convertStringToTimestamp(c.getString(2)));
            AssessmentRO.AssessmentType type =
                    AssessmentRO.AssessmentType.valueOf(c.getString(3));
            assessment.setType(type);
            c.close();
        }

        close();

        return assessment;
    }

    public List<AssessmentRO> getAll() throws ParseException {

        open();

        List<AssessmentRO> courses = new ArrayList<>();
        Cursor c = db.query(ASSESSMENT_TABLE_NAME, ALL_ASSESSMENT_COLUMNS, null,
                null,null, null, null);

        c.moveToFirst();
        for (int i=0; i<c.getCount(); i++){
            AssessmentRO assessment = new AssessmentRO();

            assessment.setId(c.getInt(0));
            assessment.setTitle(c.getString(1));
            assessment.setGoalDate(DateUtils.convertStringToTimestamp(c.getString(2)));
            AssessmentRO.AssessmentType type = AssessmentRO.AssessmentType.valueOf(c.getString(3));
            assessment.setType(type);

            courses.add(assessment);
            c.moveToNext();
        }

        c.close();
        close();

        return courses;
    }

    public void delete(int id) {
        open();
//        db.execSQL("delete from " + COURSE_TABLE_NAME + " where " + COURSE_TABLE_ID +"=" + id);
        close();
    }

    public void update(AssessmentRO assessment) {
        open();
//
//        String whereClause = "_id=" + assessment.getId();
//        ContentValues cv = new ContentValues();
//        cv.put(COURSE_TABLE_TITLE, assessment.getTitle());
//        cv.put(COURSE_TABLE_START, DateUtils.convertTimestampToString(assessment.getStart()));
//        cv.put(COURSE_TABLE_END, DateUtils.convertTimestampToString(assessment.getEnd()));
//        cv.put(COURSE_TABLE_STATUS, assessment.getStatus());
//        cv.put(COURSE_TABLE_MENTOR, assessment.getMentor());
//        cv.put(COURSE_TABLE_NOTES, assessment.getNotes());
//        cv.put(COURSE_TABLE_START_ALERT, DateUtils.convertTimestampToString(assessment.getStartAlert()));
//        cv.put(COURSE_TABLE_END_ALERT, DateUtils.convertTimestampToString(assessment.getEndAlert()));
//
//        db.update(COURSE_TABLE_NAME, cv, whereClause, null);

        close();
    }
}
