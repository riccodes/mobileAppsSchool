package com.school.codes.ric.mobileappsproject.resource;

import java.sql.Timestamp;

public class AssessmentRO extends BaseRO {

    private AssessmentType type;
    private Timestamp goalDate;

    public AssessmentRO(String title, AssessmentType type, Timestamp goalDate) {
        super(title, null, null);
        this.type = type;
        this.goalDate = goalDate;
    }

    public AssessmentRO() {
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public Timestamp getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(Timestamp goalDate) {
        this.goalDate = goalDate;
    }

    public enum AssessmentType {
        OBJECTIVE,
        PERFORMANCE
    }
}
