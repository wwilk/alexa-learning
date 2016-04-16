package com.mpw.repetition;

import com.mpw.config.AbstractRepository;
import com.mysema.query.types.path.EntityPathBase;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import com.mpw.repetition.Repetition.RepetitionStatus;

import java.util.Date;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Repository
public class RepetitionRepository extends AbstractRepository<Repetition> {

    private static final QRepetition REPETITION = QRepetition.repetition;

    public List<Repetition> findRepeated(int cardId){
        return query().where(REPETITION.status.eq(RepetitionStatus.REPEATED))
                .where(REPETITION.cardId.eq(cardId))
                .list(REPETITION);
    }

    public long countForDate(Date date){
        Date from = new DateTime(date.getTime()).withTimeAtStartOfDay().toDate();
        Date after = new DateTime(date.getTime()).plusDays(1).minusSeconds(1).withTimeAtStartOfDay().toDate();
        return query().where(REPETITION.status.eq(RepetitionStatus.PLANNED))
                .where(REPETITION.date.between(from, after)).count();
    }

    public Repetition findNextPlanned(int cardId){
        return query().where(REPETITION.cardId.eq(cardId))
                .where(REPETITION.status.eq(RepetitionStatus.PLANNED))
                .singleResult(REPETITION);
    }

    @Override
    protected EntityPathBase<Repetition> getPathBase() {
        return REPETITION;
    }

}
