package com.mpw.repetition;

import java.util.*;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Service
public class DefaultNextRepetitionService implements NextRepetitionService {
	
	private int [] gradesWeights;
	private static final int MAX_REPETITIONS_NUMBER = 5;

	public DefaultNextRepetitionService(){
		this.gradesWeights = new int[]{5, 4, 3, 2, 1};
	}

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
    	
    	DateTime dtOrg = new DateTime().withTimeAtStartOfDay();
    	DateTime dtPlusOne = dtOrg.plusDays(addedDays);
    	
    	return dtPlusOne.toDate();
    }
    
    private int countNumberOfDaysToBeAdded(List<Repetition> repetitions){
    	int nominator = 0;
    	int denominator = 0;
    	
    	int numberOfRepetitions = repetitions.size();
    	int numberOfConsideredRepetitions = (numberOfRepetitions > MAX_REPETITIONS_NUMBER) ? MAX_REPETITIONS_NUMBER : numberOfRepetitions;
    	
    	Collections.sort(repetitions, Comparator.reverseOrder());

		Repetition lastRepetition = repetitions.get(0);

    	if (Objects.equals(1, lastRepetition.getUserGrade())){
    		return 0;
    	}
    	
    	for (int i = 0; i < numberOfConsideredRepetitions; ++i){
    		nominator += gradesWeights[i] * repetitions.get(i).getUserGrade();
    		denominator += gradesWeights[i];
    	}
    	return nominator/denominator;
    }
}
