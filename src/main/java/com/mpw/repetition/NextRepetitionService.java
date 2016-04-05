package com.mpw.repetition;

import java.util.Date;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public interface NextRepetitionService {
    Date nextRepetition(List<Repetition> repetitions);
}
