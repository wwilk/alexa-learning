package com.mpw;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.mpw.Repetition.RepetitionStatus;

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
        algorithm = new DefaultNextRepetitionService();
    }

    @Test
    public void test(){
        DateTime now = new DateTime();
        DateTime yesterday = new DateTime().minusDays(1);
        DateTime todayMidnight = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrowNoon = yesterday.withHourOfDay(12).withMinuteOfHour(0); // notice immutability

        Repetition falseNowPlanned = new Repetition(now.toDate(), false, RepetitionStatus.PLANNED);
        Repetition trueYesterdayRepeated = new Repetition(yesterday.toDate(), true, RepetitionStatus.REPEATED);
        Repetition falseTodayMidnightRepeated = new Repetition(todayMidnight.toDate(), false, RepetitionStatus.REPEATED);
        Repetition falseTomorrowNoonPlanned = new Repetition(tomorrowNoon.toDate(), null, RepetitionStatus.PLANNED);

        List<Repetition> repetitions = new ArrayList<>();
        repetitions.add(falseNowPlanned);
        repetitions.add(trueYesterdayRepeated);
        repetitions.add(falseTodayMidnightRepeated);
        repetitions.add(falseTomorrowNoonPlanned);

        Collections.sort(repetitions); // sorts in order based on implementation of Comparable interface

        Date result = algorithm.nextRepetition(repetitions);

        assertThat(result).isNull();
    }
}
