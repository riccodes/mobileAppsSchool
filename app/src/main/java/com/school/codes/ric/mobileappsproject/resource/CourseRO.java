package com.school.codes.ric.mobileappsproject.resource;

import java.sql.Timestamp;

public class CourseRO extends BaseRO {

    private String status;
    private String mentor;
    private String notes;
    private Timestamp startAlert;
    private Timestamp endAlert;
    private int termId;

    public CourseRO(String title, Timestamp start, Timestamp end) {
        super(title, start, end);
    }

    public CourseRO(String title, Timestamp start, Timestamp end, String status, String mentor,
                    String notes, Timestamp startAlert, Timestamp endAlert){
        this(title, start, end);
        this.status = status;
        this.mentor = mentor;
        this.notes = notes;
        this.startAlert = startAlert;
        this.endAlert = endAlert;
    }

    public CourseRO() {
        super();
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getStartAlert() {
        return startAlert;
    }

    public void setStartAlert(Timestamp startAlert) {
        this.startAlert = startAlert;
    }

    public Timestamp getEndAlert() {
        return endAlert;
    }

    public void setEndAlert(Timestamp endAlert) {
        this.endAlert = endAlert;
    }

    @Override
    public String toString() {
        return "CourseRO{" +
                "status='" + status + '\'' +
                ", mentor='" + mentor + '\'' +
                ", notes='" + notes + '\'' +
                ", startAlert=" + startAlert +
                ", endAlert=" + endAlert +
                '}';
    }
}
