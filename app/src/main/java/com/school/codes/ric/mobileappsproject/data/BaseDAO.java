package com.school.codes.ric.mobileappsproject.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDAO {

    public SQLiteDatabase db;
    private DBHelper helper;

    public BaseDAO(Context context){
        helper = new DBHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public void clearDB(String table) {
        open();
        db.execSQL("delete from " + table);
        close();
    }

}
