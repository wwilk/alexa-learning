package com.mpw.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestBody Card card){
        cardService.save(card);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Card> findAll(){
        return cardService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pending")
    public List<Card> findPending(){
        return cardService.findAll();
    }
}
