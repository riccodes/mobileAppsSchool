package com.school.codes.ric.mobileappsproject.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDAO {

    private static final String TAG = BaseDAO.class.getSimpleName();

    SQLiteDatabase db;
    private DBHelper helper;
    private Context mContext;

    BaseDAO(Context context) {
        this.mContext = context;
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
