package com.mpw.repetition;

import java.util.Date;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class Repetition implements Comparable<Repetition>{
    private Date date;
    private int userGrade;
    private RepetitionStatus status;

    public Repetition(Date date, int userGrade){
        this.date = date;
        this.userGrade = userGrade;
        this.status = RepetitionStatus.PLANNED;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getUserGrade(){
    	return userGrade;
    }
    
    public RepetitionStatus getStatus() {
        return status;
    }

    public void setStatus(RepetitionStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Repetition o) {
        return this.date.compareTo(o.date);
    }

    public enum RepetitionStatus{
        PLANNED, REPEATED
    }
}
