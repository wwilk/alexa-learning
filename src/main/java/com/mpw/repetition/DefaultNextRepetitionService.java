package com.mpw.repetition;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import org.joda.time.DateTime;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class DefaultNextRepetitionService implements NextRepetitionService {
	
	private int [] gradesWeights;
	private static final int MAX_REPETITIONS_NUMBER = 5;
	
	public DefaultNextRepetitionService(int [] gradesWeights) {
		this.gradesWeights = gradesWeights;
	}
    @Override
    public Date nextRepetition(List<Repetition> repetitions) {
    	
    	if (0 == repetitions.size()){
    		DateTime now = new DateTime().withTimeAtStartOfDay();
    		return now.toDate();
    	}
    	
    	int addedDays = 0;
    	addedDays = countNumberOfDaysToBeAdded(repetitions);
    	
    	Date d = repetitions.get(0).getDate();
    	DateTime dtOrg = new DateTime(d.getTime());
    	DateTime dtPlusOne = dtOrg.plusDays(addedDays);
    	
    	return dtPlusOne.toDate();
    }
    
    private int countNumberOfDaysToBeAdded(List<Repetition> repetitions){
    	int nominator = 0;
    	int denominator = 0;
    	
    	int numberOfRepetitions = repetitions.size();
    	int numberOfConsideredRepetitions = (numberOfRepetitions > MAX_REPETITIONS_NUMBER) ? MAX_REPETITIONS_NUMBER : numberOfRepetitions;
    	
    	Collections.sort(repetitions, Comparator.reverseOrder());
    	
    	for (int i = 0; i < numberOfConsideredRepetitions; ++i){
    		nominator += gradesWeights[i] * repetitions.get(i).getUserGrade();
    		denominator += gradesWeights[i];
    	}
    	return nominator/denominator;
    }
}
