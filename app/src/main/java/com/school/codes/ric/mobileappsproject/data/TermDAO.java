package com.school.codes.ric.mobileappsproject.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.school.codes.ric.mobileappsproject.resource.TermRO;
import com.school.codes.ric.mobileappsproject.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.school.codes.ric.mobileappsproject.util.Constants.*;

public class TermDAO extends BaseDAO {

    private final String[] ALL_TERM_COLUMNS = {
            TERM_TABLE_ID,
            TERM_TABLE_TITLE,
            TERM_TABLE_START,
            TERM_TABLE_END
    };

    public TermDAO(Context context) {
        super(context);
    }

    public void add(TermRO term) {
        open();

        ContentValues cv = new ContentValues();

        cv.put(TERM_TABLE_TITLE, term.getTitle());
        cv.put(TERM_TABLE_START, DateUtils.convertTimestampToString(term.getStart()));
        cv.put(TERM_TABLE_END, DateUtils.convertTimestampToString(term.getEnd()));

        db.insert(TERM_TABLE_NAME, null, cv);

        close();
    }

    public TermRO get(int id) throws ParseException {
        open();

        String whereClause = TERM_TABLE_ID + "=?";
        String[] args = {id+""};
        Cursor c = db.query(TERM_TABLE_NAME, ALL_TERM_COLUMNS, whereClause, args,
                null, null, null);

        TermRO term = null;

        if(c.moveToFirst()){
            term = new TermRO();
            term.setId(c.getInt(0));
            term.setTitle(c.getString(1));
            term.setStart(DateUtils.convertStringToTimestamp(c.getString(2)));
            term.setEnd(DateUtils.convertStringToTimestamp(c.getString(3)));
            c.close();
        }

        close();

        return term;
    }

    public List<TermRO> getAll() throws ParseException {

        open();

        List<TermRO> terms = new ArrayList<>();
        Cursor c = db.query(TERM_TABLE_NAME, ALL_TERM_COLUMNS, null, null,
                null, null, null);

        c.moveToFirst();
        for (int i=0; i<c.getCount(); i++){
            TermRO term = new TermRO();

            term.setId(c.getInt(0));
            term.setTitle(c.getString(1));
            term.setStart(DateUtils.convertStringToTimestamp(c.getString(2)));
            term.setEnd(DateUtils.convertStringToTimestamp(c.getString(3)));

            terms.add(term);
            c.moveToNext();
        }

        c.close();
        close();

        return terms;
    }

    public void delete(int id) {
        open();
        db.execSQL("delete from " + TERM_TABLE_NAME + " where " + TERM_TABLE_ID +"=" + id);
        close();
    }

    public void update(TermRO term) {
        open();

        String whereClause = "_id=" + term.getId();
        ContentValues cv = new ContentValues();
        cv.put(TERM_TABLE_TITLE, term.getTitle());
        cv.put(TERM_TABLE_START, DateUtils.convertTimestampToString(term.getStart()));
        cv.put(TERM_TABLE_END, DateUtils.convertTimestampToString(term.getEnd()));

        db.update(TERM_TABLE_NAME, cv, whereClause, null);

        close();
    }
}
