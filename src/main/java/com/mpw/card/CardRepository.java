package com.mpw.card;

import com.mpw.jpa.AbstractRepository;
import com.mpw.repetition.QRepetition;
import com.mpw.repetition.Repetition.RepetitionStatus;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.EntityPathBase;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@Repository
public class CardRepository extends AbstractRepository<Card> {

    private static final QCard CARD = QCard.card;
    private static final QRepetition REPETITION = QRepetition.repetition;

    public List<Card> findPlannedBefore(Date date){
        return query()
                .where(repetitionSubquery(date, REPETITION.cardId.eq(CARD.id)))
                .list(CARD);
    }

    private BooleanExpression repetitionSubquery(Date date, BooleanExpression joinExpression){
        return new JPASubQuery().from(REPETITION)
                .where(joinExpression)
                .where(REPETITION.status.eq(RepetitionStatus.PLANNED))
                .where(REPETITION.date.before(date)).exists();
    }

    @Override
    protected EntityPathBase<Card> getPathBase() {
        return CARD;
    }
}
