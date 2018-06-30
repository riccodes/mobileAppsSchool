package com.school.codes.ric.mobileappsproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.school.codes.ric.mobileappsproject.resource.CourseRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_END;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_END_ALERT;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_MENTOR;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_NOTES;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_START;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_START_ALERT;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_STATUS;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_TERM_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.COURSE_TABLE_TITLE;

public class CourseDAO extends BaseDAO {

    private final String[] ALL_COURSE_COLUMNS = {
            COURSE_TABLE_ID,
            COURSE_TABLE_TITLE,
            COURSE_TABLE_START,
            COURSE_TABLE_END,
            COURSE_TABLE_STATUS,
            COURSE_TABLE_MENTOR,
            COURSE_TABLE_NOTES,
            COURSE_TABLE_START_ALERT,
            COURSE_TABLE_END_ALERT,
            COURSE_TABLE_TERM_ID
    };

    public CourseDAO(Context context) {
        super(context);
    }

    public void add(CourseRO course) {
        open();

        ContentValues cv = new ContentValues();

        cv.put(COURSE_TABLE_TITLE, course.getTitle());
        cv.put(COURSE_TABLE_START, DateUtils.convertTimestampToString(course.getStart()));
        cv.put(COURSE_TABLE_END, DateUtils.convertTimestampToString(course.getEnd()));
        cv.put(COURSE_TABLE_STATUS, course.getStatus());
        cv.put(COURSE_TABLE_MENTOR, course.getMentor());
        cv.put(COURSE_TABLE_NOTES, course.getNotes());
        cv.put(COURSE_TABLE_START_ALERT,
                DateUtils.convertTimestampToString(course.getStartAlert()));
        cv.put(COURSE_TABLE_END_ALERT,
                DateUtils.convertTimestampToString(course.getEndAlert()));

        db.insert(COURSE_TABLE_NAME, null, cv);

        close();
    }

    public void addWithId(CourseRO course) {
        open();

        ContentValues cv = new ContentValues();

        cv.put(COURSE_TABLE_ID, course.getId());
        cv.put(COURSE_TABLE_TITLE, course.getTitle());
        cv.put(COURSE_TABLE_START, DateUtils.convertTimestampToString(course.getStart()));
        cv.put(COURSE_TABLE_END, DateUtils.convertTimestampToString(course.getEnd()));
        cv.put(COURSE_TABLE_STATUS, course.getStatus());
        cv.put(COURSE_TABLE_MENTOR, course.getMentor());
        cv.put(COURSE_TABLE_NOTES, course.getNotes());
        cv.put(COURSE_TABLE_START_ALERT,
                DateUtils.convertTimestampToString(course.getStartAlert()));
        cv.put(COURSE_TABLE_END_ALERT,
                DateUtils.convertTimestampToString(course.getEndAlert()));

        db.insert(COURSE_TABLE_NAME, null, cv);

        close();
    }

    public CourseRO get(int id) throws ParseException {
        open();

        String whereClause = COURSE_TABLE_ID + "=?";
        String[] args = {id+""};
        Cursor c = db.query(COURSE_TABLE_NAME, ALL_COURSE_COLUMNS, whereClause, args,
                null, null, null);

        CourseRO course = null;

        if(c.moveToFirst()){
            course = new CourseRO();
            course.setId(c.getInt(0));
            course.setTitle(c.getString(1));
            course.setStart(DateUtils.convertStringToTimestamp(c.getString(2)));
            course.setEnd(DateUtils.convertStringToTimestamp(c.getString(3)));
            course.setStatus(c.getString(4));
            course.setMentor(c.getString(5));
            course.setNotes(c.getString(6));
            course.setStartAlert(DateUtils.convertStringToTimestamp(c.getString(7)));
            course.setEndAlert(DateUtils.convertStringToTimestamp(c.getString(8)));
            course.setTermId(c.getInt(9));
            c.close();
        }

        close();

        return course;
    }

    public List<CourseRO> getAll() throws ParseException {

        open();

        List<CourseRO> courses = new ArrayList<>();
        Cursor c = db.query(COURSE_TABLE_NAME, ALL_COURSE_COLUMNS, null, null,
                null, null, null);

        c.moveToFirst();
        for (int i=0; i<c.getCount(); i++){
            CourseRO course = new CourseRO();

            course.setId(c.getInt(0));
            course.setTitle(c.getString(1));
            course.setStart(DateUtils.convertStringToTimestamp(c.getString(2)));
            course.setEnd(DateUtils.convertStringToTimestamp(c.getString(3)));
            course.setStatus(c.getString(4));
            course.setMentor(c.getString(5));
            course.setNotes(c.getString(6));
            course.setStartAlert(DateUtils.convertStringToTimestamp(c.getString(7)));
            course.setEndAlert(DateUtils.convertStringToTimestamp(c.getString(8)));
            course.setTermId(c.getInt(9));

            courses.add(course);
            c.moveToNext();
        }

        c.close();
        close();

        return courses;
    }

    public void delete(int id) {
        open();
        db.execSQL("delete from " + COURSE_TABLE_NAME + " where " + COURSE_TABLE_ID +"=" + id);
        close();
    }

    public void update(CourseRO course) {
        open();

        String whereClause = "_id=" + course.getId();
        ContentValues cv = new ContentValues();
        cv.put(COURSE_TABLE_TERM_ID, course.getTermId());
        cv.put(COURSE_TABLE_TITLE, course.getTitle());
        cv.put(COURSE_TABLE_START, DateUtils.convertTimestampToString(course.getStart()));
        cv.put(COURSE_TABLE_END, DateUtils.convertTimestampToString(course.getEnd()));
        cv.put(COURSE_TABLE_STATUS, course.getStatus());
        cv.put(COURSE_TABLE_MENTOR, course.getMentor());
        cv.put(COURSE_TABLE_NOTES, course.getNotes());
        cv.put(COURSE_TABLE_START_ALERT,
                DateUtils.convertTimestampToString(course.getStartAlert()));
        cv.put(COURSE_TABLE_END_ALERT,
                DateUtils.convertTimestampToString(course.getEndAlert()));

        db.update(COURSE_TABLE_NAME, cv, whereClause, null);

        close();
    }

    public List<CourseRO> getAllAssociated(int termId) throws ParseException {
        open();

        String whereClause = COURSE_TABLE_TERM_ID + "=?";
        String[] args = {termId + ""};

        Cursor c = db.query(COURSE_TABLE_NAME, ALL_COURSE_COLUMNS, whereClause, args,
                null, null, null);

        List<CourseRO> courses = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            CourseRO course = new CourseRO();
            course.setId(c.getInt(0));
            course.setTitle(c.getString(1));
            course.setStart(DateUtils.convertStringToTimestamp(c.getString(2)));
            course.setEnd(DateUtils.convertStringToTimestamp(c.getString(3)));
            course.setStatus(c.getString(4));
            course.setMentor(c.getString(5));
            course.setNotes(c.getString(6));
            course.setStartAlert(DateUtils.convertStringToTimestamp(c.getString(7)));
            course.setEndAlert(DateUtils.convertStringToTimestamp(c.getString(8)));
            course.setTermId(c.getInt(9));

            courses.add(course);
            c.moveToNext();
        }

        c.close();
        close();

        return courses;
    }

    public void disassociate(CourseRO course) {
        open();

        delete(course.getId());
        addWithId(copyCourse(course));

        close();
    }

    private CourseRO copyCourse(CourseRO course) {
        CourseRO c = new CourseRO();

        c.setId(course.getId());
        c.setTitle(course.getTitle());
        c.setStart(course.getStart());
        c.setEnd(course.getEnd());
        c.setStatus(course.getStatus());
        c.setMentor(course.getMentor());
        c.setNotes(course.getNotes());
        c.setStartAlert(course.getStartAlert());
        c.setEndAlert(course.getEndAlert());

        return c;
    }
}
