package com.school.codes.ric.mobileappsproject.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
//import  org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilTest {

    @Test(expected = ParseException.class)
    public void convert_string_to_timestamp() throws ParseException {
        String time = "30-oct-1984";
        Timestamp timestamp = DateUtils.convertStringToTimestamp(time);
        assertEquals("TimeStamp day wrong", 30, timestamp.getDate());
        assertEquals("TimeStamp month wrong", 10, timestamp.getMonth() +1);
        assertEquals("TimeStamp year wrong", 84, timestamp.getYear());

        String expected = "1984-10-30 00:00:00.0";
        assertEquals("Timestamp string was wrong", expected, timestamp.toString());

        DateUtils.convertStringToTimestamp("sdfsdfs");

    }

    @Test
    public void convert_timestap_to_string() throws ParseException {
        String date = "01-Jan-2016";
        Timestamp timestamp = DateUtils.convertStringToTimestamp(date);

        String received = DateUtils.convertTimestampToString(timestamp);
        assertEquals("Dates were incorrect", date, received);
    }
}