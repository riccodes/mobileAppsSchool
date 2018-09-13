package com.school.codes.ric.mobileappsproject.util;

public class Constants {

    public static final String TERM_TABLE_NAME = "terms";
    public static final String TERM_TABLE_ID = "_id";
    public static final String TERM_TABLE_TITLE = "title";
    public static final String TERM_TABLE_START = "start_date";
    public static final String TERM_TABLE_END = "end_date";

    public static final String COURSE_TABLE_NAME = "courses";
    public static final String COURSE_TABLE_ID = "_id";
    public static final String COURSE_TABLE_TERM_ID = "term_id";
    public static final String COURSE_TABLE_TITLE = "title";
    public static final String COURSE_TABLE_START = "start_date";
    public static final String COURSE_TABLE_END = "end_date";
    public static final String COURSE_TABLE_STATUS = "status";
    public static final String COURSE_TABLE_MENTOR = "mentor";
    public static final String COURSE_TABLE_NOTES = "notes";
    public static final String COURSE_TABLE_START_ALERT = "start_alert";
    public static final String COURSE_TABLE_END_ALERT = "end_alert";

    public static final String ASSESSMENT_TABLE_NAME = "assessments";
    public static final String ASSESSMENT_TABLE_ID = "_id";
    public static final String ASSESSMENT_TABLE_COURSE_ID = "course_id";
    public static final String ASSESSMENT_TABLE_TITLE = "title";
    public static final String ASSESSMENT_TABLE_GOAL = "goal_date";
    public static final String ASSESSMENT_TABLE_TYPE = "type";

    public static final String ASSOCIATE_TEXT = "Swipe to add or remove ";

    public static final String DROPPED_STATUS = "DROPPED";
    public static final String IN_PROGRESS_STATUS = "IN PROGRESS";
    public static final String COMPLETE_STATUS = "COMPLETE";
    public static final String PLAN_STATUS = "PLANNED";

    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_TITLE_GOAL = "Assessment's Goal Date is today";
    public static final String NOTIFICATION_TITLE_START = "Course Start Date Alert";
    public static final String NOTIFICATION_TITLE_END = "Course End Date Alert";
    public static final int NOTIFICATION_ID_OFFSET = 100;

}
