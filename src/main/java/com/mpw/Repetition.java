package com.mpw;

import java.util.Date;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class Repetition implements Comparable<Repetition>{
    private Date date;
    private Boolean correct;
    private RepetitionStatus status;

    public Repetition(Date date, Boolean correct, RepetitionStatus status){
        this.date = date;
        this.correct = correct;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RepetitionStatus getStatus() {
        return status;
    }

    public void setStatus(RepetitionStatus status) {
        this.status = status;
    }

    public Boolean isCorrect(){
        return correct;
    }

    public void setCorrect(Boolean correct){
        this.correct = correct;
    }

    @Override
    public int compareTo(Repetition o) {
        return this.date.compareTo(o.date);
    }

    public enum RepetitionStatus{
        PLANNED, REPEATED
    }
}
