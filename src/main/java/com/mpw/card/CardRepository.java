package com.mpw.card;


import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public interface CardRepository extends Repository<Card, Integer>{
    void save(Card card);

    List<Card> findAll();
}
