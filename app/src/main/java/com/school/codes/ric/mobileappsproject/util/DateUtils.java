package com.school.codes.ric.mobileappsproject.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat DB_SDF =
            new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    public static Timestamp convertStringToTimestamp(String s) throws ParseException {
        Date date = DB_SDF.parse(s);
        return new Timestamp(date.getTime());
    }

    public static String convertTimestampToString(Timestamp timestamp){
        Date date = new Date(timestamp.getTime());
        return DB_SDF.format(date);
    }

}
