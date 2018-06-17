package com.school.codes.ric.mobileappsproject.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import static com.school.codes.ric.mobileappsproject.util.Constants.DB_SDF;

public class DateUtils {

    public static Timestamp convertStringToTimestamp(String s) throws ParseException {
        Date date = DB_SDF.parse(s);
        return new Timestamp(date.getTime());
    }

    public static String convertTimestampToString(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        return DB_SDF.format(date);
    }

}
