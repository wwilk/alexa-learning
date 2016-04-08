package com.mpw.repetition;

import com.mpw.config.AbstractRepository;
import com.mysema.query.types.path.EntityPathBase;
import org.springframework.stereotype.Repository;
import com.mpw.repetition.Repetition.RepetitionStatus;

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

    public Repetition findCurrent(int cardId){
        return query().where(REPETITION.cardId.eq(cardId))
                .where(REPETITION.status.eq(RepetitionStatus.PLANNED))
                .singleResult(REPETITION);
    }

    public Repetition findById(int repetitionId){
        return query().where(REPETITION.id.eq(repetitionId))
                .singleResult(REPETITION);
    }

    @Override
    protected EntityPathBase<Repetition> getPathBase() {
        return REPETITION;
    }

}
