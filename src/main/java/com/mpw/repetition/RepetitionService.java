package com.mpw.repetition;

import com.mpw.card.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Service
@Transactional
public class RepetitionService {

    private final RepetitionRepository repetitionRepository;
    private final NextRepetitionService nextRepetitionService;

    @Autowired
    public RepetitionService(RepetitionRepository repetitionRepository, NextRepetitionService nextRepetitionService) {
        this.repetitionRepository = repetitionRepository;
        this.nextRepetitionService = nextRepetitionService;
    }

    public void grade(int grade, int cardId){
        Repetition repetition = findCurrent(cardId);
        repetition.setUserGrade(grade);
        repetition.setStatus(Repetition.RepetitionStatus.REPEATED);
    }

    public void scheduleNext(int cardId){
        List<Repetition> repetitions = repetitionRepository.findRepeated(cardId);
        Date nextDate = nextRepetitionService.nextRepetition(repetitions);

        Repetition next = nextRepetition(cardId, nextDate);
        repetitionRepository.save(next);
    }

    public Repetition findCurrent(int cardId){
        return repetitionRepository.findCurrent(cardId);
    }

    private Repetition nextRepetition(int cardId, Date nextDate){
        Repetition nextRepetition = new Repetition();
        nextRepetition.setDate(nextDate);
        nextRepetition.setStatus(Repetition.RepetitionStatus.PLANNED);
        nextRepetition.setCardId(cardId);
        return nextRepetition;
    }

}
