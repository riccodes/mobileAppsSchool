package com.school.codes.ric.mobileappsproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_COURSE_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_GOAL;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_TITLE;
import static com.school.codes.ric.mobileappsproject.util.Constants.ASSESSMENT_TABLE_TYPE;
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
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_END;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_ID;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_NAME;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_START;
import static com.school.codes.ric.mobileappsproject.util.Constants.TERM_TABLE_TITLE;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="project_db";
    private static final int VERSION = 2;

    private static final String DROP ="DROP TABLE IF EXISTS ";
    private static final String CREATE_TERM_TABLE = "create table " +
            TERM_TABLE_NAME +
            "(" +
            TERM_TABLE_ID +
            " integer not null unique primary key, " +
            TERM_TABLE_TITLE +
            " text not null, " +
            TERM_TABLE_START +
            " datetime not null, " +
            TERM_TABLE_END +
            " datetime not null " +
            ")";
    private static final String CREATE_COURSE_TABLE = "create table " +
            COURSE_TABLE_NAME +
            "(" +
            COURSE_TABLE_ID +
            " integer not null unique primary key, " +
            COURSE_TABLE_TITLE +
            " text not null, " +
            COURSE_TABLE_START +
            " datetime not null, " +
            COURSE_TABLE_END +
            " datetime not null, " +
            COURSE_TABLE_STATUS +
            " text not null, " +
            COURSE_TABLE_MENTOR +
            " text not null, " +
            COURSE_TABLE_NOTES +
            " text not null, " +
            COURSE_TABLE_START_ALERT +
            " datetime not null, " +
            COURSE_TABLE_END_ALERT +
            " datetime not null, " +
            COURSE_TABLE_TERM_ID +
            " integer, " +
            " FOREIGN KEY(" + COURSE_TABLE_TERM_ID + ")" +
            " REFERENCES " + TERM_TABLE_NAME + "(" + TERM_TABLE_ID + ")" +
            ")";
    private static final String CREATE_ASSESSMENT_TABLE= "create table " +
            ASSESSMENT_TABLE_NAME +
            "(" +
            ASSESSMENT_TABLE_ID +
            " integer not null unique primary key, " +
            ASSESSMENT_TABLE_TITLE +
            " text not null, " +
            ASSESSMENT_TABLE_GOAL +
            " datetime not null, " +
            ASSESSMENT_TABLE_TYPE +
            " text not null, " +
            ASSESSMENT_TABLE_COURSE_ID +
            " integer, " +
            " FOREIGN KEY(" + ASSESSMENT_TABLE_COURSE_ID + ")" +
            " REFERENCES " + COURSE_TABLE_NAME + "(" + COURSE_TABLE_ID + ")" +
            ")";

    DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ASSESSMENT_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_TERM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP + ASSESSMENT_TABLE_NAME);
        db.execSQL(DROP + COURSE_TABLE_NAME);
        db.execSQL(DROP + TERM_TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }
}
