package com.school.codes.ric.mobileappsproject.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDAO {

    SQLiteDatabase db;
    private DBHelper helper;

    BaseDAO(Context context) {
        helper = new DBHelper(context);
    }

    void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    void close() {
        helper.close();
    }

    public void clearDB(String table) {
        open();
        db.execSQL("delete from " + table);
        close();
    }

}
