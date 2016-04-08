package com.mpw.repetition;

import static org.assertj.core.api.Assertions.*;

import com.mpw.HackathonApplication;
import com.mpw.card.Card;
import com.mpw.card.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HackathonApplication.class)
@WebAppConfiguration
@IntegrationTest
public class RepetitionServiceIT{

    @Autowired
    private RepetitionService repetitionService;
    @Autowired
    private CardService cardService;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void test(){
        Card card = new Card();
        card.setAnswer("test");
        card.setQuestion("test");

        cardService.save(card);

        assertThat(card.getId()).isNotEqualTo(0);

        List<Card> planned = cardService.findPlannedForToday();

        assertThat(planned).hasSize(1);
        assertThat(planned.get(0).getId()).isEqualTo(card.getId());

        assertThat(repetitionService.findCurrent(card.getId())).isNotNull();

        repetitionService.grade(1, card.getId());

        assertThat(cardService.findPlannedForToday()).isEmpty();

        repetitionService.scheduleNext(card.getId());

        planned = cardService.findPlannedForToday();

        assertThat(planned).hasSize(1);

    }
}
