package com.mpw;

import com.mpw.repetition.DefaultNextRepetitionService;
import com.mpw.repetition.Repetition;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.mpw.repetition.Repetition.RepetitionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class DefaultNextRepetitionServiceTest {

    private DefaultNextRepetitionService algorithm;

    @Before
    public void setUp(){
    	
    	int [] gradesWeights = {5, 4, 3, 2, 1};
        algorithm = new DefaultNextRepetitionService(gradesWeights);
    
    }
    
    @Test
    public void shouldReturnDatePlusOneDayWhenThereIsOnlyOneGradeEqualToOne(){
    	
    	int grade = 1;
    	int numberOfAddedDays = 1;
    	
    	DateTime now = new DateTime();
    	DateTime expectedDate = now.plusDays(numberOfAddedDays);
    	
    	Repetition repetition = new Repetition(now.toDate(), grade);
    	
    	List<Repetition> singleRepetitionList = new ArrayList<>();
		singleRepetitionList.add(repetition);
		
		assertThat(algorithm.nextRepetition(singleRepetitionList)).isEqualTo(expectedDate.toDate());
    }
    
    @Test
    public void shouldReturnDatePlusTwoDaysWhenThereAreOnlyGradesEqualToTwo(){
    	
    	int grade = 2;
    	int numberOfAddedDays = 2;
    	int repetitionsNumber = 5;
    	DateTime now = new DateTime();
		DateTime expectedDate = now.plusDays(numberOfAddedDays);
    	
    	List<Repetition> repetitions = new ArrayList<>();
    	
    	for (int i = 0; i < repetitionsNumber; ++i){
    		repetitions.add(new Repetition(now.minusDays(i).toDate(), grade));
    	}
    	
		assertThat(algorithm.nextRepetition(repetitions)).isEqualTo(expectedDate.toDate());
    }
    
    @Test
    public void shouldReturnDatePlusTwoDaysWhenThereAreFiveGradesEqualToTwoAndTheEarliestDifferent(){
    	
    	int grade = 2;
    	int gradeDifferentFromTwo = 5;
    	int minusDaysForGradeDifferentFromTwo = 100;
    	int numberOfAddedDays = 2;
    	int repetitionsNumber = 5;
    	
    	DateTime now = new DateTime();
		DateTime expectedDate = now.plusDays(numberOfAddedDays);
    	
    	List<Repetition> repetitions = new ArrayList<>();
    	
    	for (int i = 0; i < repetitionsNumber; ++i){
    		repetitions.add(new Repetition(now.minusDays(i).toDate(), grade));
    	}
    	repetitions.add(new Repetition(now.minusDays(minusDaysForGradeDifferentFromTwo).toDate(), gradeDifferentFromTwo));
		
    	assertThat(algorithm.nextRepetition(repetitions)).isEqualTo(expectedDate.toDate());
    }
    
    @Test
    public void shouldReturnTodaysDateWithTimeAtStartOfDayWhenRepetitionsListIsEmpty(){
    	
    	DateTime todaysMidnight = new DateTime().withTimeAtStartOfDay();
    	
    	List<Repetition> repetitions = new ArrayList<>();
		
    	assertThat(algorithm.nextRepetition(repetitions)).isEqualTo(todaysMidnight.toDate());
    }
}
