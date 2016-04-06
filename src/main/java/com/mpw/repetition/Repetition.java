package com.mpw.repetition;

import java.util.Date;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class Repetition implements Comparable<Repetition>{
    private Date date;
    private int userGrade;

    public Repetition(Date date, int userGrade){
        this.date = date;
        this.userGrade = userGrade;
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

    @Override
    public int compareTo(Repetition o) {
        return this.date.compareTo(o.date);
    }

    public enum RepetitionStatus{
        PLANNED, REPEATED
    }
}
