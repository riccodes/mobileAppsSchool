package com.school.codes.ric.mobileappsproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.school.codes.ric.mobileappsproject.util.Constants.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="project_db";
    private static final int VERSION = 1;

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


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TERM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP + TERM_TABLE_NAME);
        onCreate(db);
    }
}
