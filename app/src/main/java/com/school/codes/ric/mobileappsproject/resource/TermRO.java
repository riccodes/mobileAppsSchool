package com.school.codes.ric.mobileappsproject.resource;

import java.sql.Timestamp;

public class TermRO extends BaseRO {

    public TermRO(String title, Timestamp start, Timestamp end) {
        super(title, start, end);
    }

    public TermRO() {}
}
