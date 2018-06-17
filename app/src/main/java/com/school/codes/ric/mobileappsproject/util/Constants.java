package com.school.codes.ric.mobileappsproject.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
    public static final SimpleDateFormat DB_SDF =
            new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    public static final String TERM_TABLE_NAME = "terms";
    public static final String TERM_TABLE_ID = "_id";
    public static final String TERM_TABLE_TITLE = "title";
    public static final String TERM_TABLE_START = "start_date";
    public static final String TERM_TABLE_END = "end_date";
}
