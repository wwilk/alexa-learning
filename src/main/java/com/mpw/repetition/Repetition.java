package com.mpw.repetition;

import com.mpw.card.Card;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Entity
public class Repetition implements Comparable<Repetition>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date date;
    private Integer userGrade;
    @Enumerated(EnumType.STRING)
    private RepetitionStatus status;
    private int cardId;

    public Repetition(){

    }

    public Repetition(Date date, Integer userGrade){
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
    
    public Integer getUserGrade(){
    	return userGrade;
    }

    public void setUserGrade(Integer userGrade){
        this.userGrade = userGrade;
    }

    public RepetitionStatus getStatus() {
        return status;
    }

    public void setStatus(RepetitionStatus status) {
        this.status = status;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @Override
    public int compareTo(Repetition o) {
        return this.date.compareTo(o.date);
    }

    public enum RepetitionStatus{
        PLANNED, REPEATED
    }
}
