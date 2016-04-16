package com.mpw.repetition;

import static org.assertj.core.api.Assertions.*;

import com.mpw.HackathonApplication;
import com.mpw.card.Card;
import com.mpw.card.CardService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.mpw.repetition.Repetition.RepetitionStatus;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HackathonApplication.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class RepetitionServiceIT{

    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private CardService cardService;

    @Test
    public void when_cards_saved_then_can_be_found(){
        assertThat(cardService.findAll()).hasSize(0);

        Card card = defaultCard();
        Card card2 = new Card("question2", "answer2");

        cardService.save(card);
        cardService.save(card2);

        List<Card> result = cardService.findAll();
        assertThat(result).hasSize(2);
        assertThat(result.stream()
                .map( c -> c.getQuestion())
                .collect(Collectors.toList())).containsOnly("question", "question2");
    }

    @Test
    public void when_card_is_saved_then_its_planned_for_the_same_day() {
        Card card = defaultCard();
        cardService.save(card);
        assertThat(card.getId()).isNotEqualTo(0);

        List<Card> planned = cardService.findPlannedForToday();
        assertThat(planned).hasSize(1);
        assertThat(planned.get(0).getId()).isEqualTo(card.getId());

        Repetition plannedRepetition = repetitionService.findCurrent(card.getId());
        assertThat(plannedRepetition).isNotNull();

        Date expectedDate = new DateTime().withTimeAtStartOfDay().toDate();
        assertThat(plannedRepetition.getDate()).isEqualTo(expectedDate);
    }

    @Test
    public void when_card_is_graded_then_its_not_planned_anymore(){
        Card card = defaultCard();
        cardService.save(card);

        repetitionService.grade(1, card.getId());

        Repetition plannedRepetition = repetitionService.findCurrent(card.getId());
        assertThat(plannedRepetition).isNull();
        assertThat(cardService.findPlannedForToday()).isEmpty();

        List<Repetition> allRepetitions = repetitionService.findAll();
        assertThat(allRepetitions).hasSize(1);
        assertThat(allRepetitions.get(0).getStatus()).isEqualTo(RepetitionStatus.REPEATED);
    }

    @Test
    public void when_card_is_graded_with_1_and_scheduled_then_its_planned_for_today() {
        Card card = defaultCard();

        cardService.save(card);

        assertThat(repetitionService.findCurrent(card.getId())).isNotNull();

        repetitionService.grade(1, card.getId());
        assertThat(cardService.findPlannedForToday()).isEmpty();

        repetitionService.scheduleNext(card.getId());

        List<Card> planned = cardService.findPlannedForToday();
        assertThat(planned).hasSize(1);
        assertThat(planned.get(0).getId()).isEqualTo(card.getId());

        Repetition nextRepetition = repetitionService.findCurrent(card.getId());
        Date todayMidnight = new DateTime().withTimeAtStartOfDay().toDate();
        assertThat(nextRepetition.getDate()).isEqualTo(todayMidnight);
    }

    @Test
    public void when_card_is_graded_with_3_and_scheduled_then_its_planned_for_a_future_day() {
        Card card = defaultCard();

        cardService.save(card);

        assertThat(repetitionService.findCurrent(card.getId())).isNotNull();

        repetitionService.grade(3, card.getId());
        assertThat(cardService.findPlannedForToday()).isEmpty();

        repetitionService.scheduleNext(card.getId());
        assertThat(cardService.findPlannedForToday()).isEmpty();

        Repetition nextRepetition = repetitionService.findCurrent(card.getId());
        Date tomorrowMidnight = new DateTime().plusDays(1).withTimeAtStartOfDay().toDate();
        assertThat(nextRepetition.getDate()).isAfter(tomorrowMidnight);
    }

    private Card defaultCard(){
        return new Card("question", "answer");
    }
}
