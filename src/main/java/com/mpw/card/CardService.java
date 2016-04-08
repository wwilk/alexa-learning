package com.mpw.card;

import com.mpw.repetition.RepetitionService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final RepetitionService repetitionService;

    @Autowired
    public CardService(CardRepository cardRepository, RepetitionService repetitionService){
        this.cardRepository = cardRepository;
        this.repetitionService = repetitionService;
    }

    public void save(Card card){
        cardRepository.save(card);
        repetitionService.scheduleNext(card.getId());
    }

    public List<Card> findPlannedForToday(){
        return cardRepository.findPlannedBefore(new DateTime().plusDays(1).withTimeAtStartOfDay().toDate());
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
