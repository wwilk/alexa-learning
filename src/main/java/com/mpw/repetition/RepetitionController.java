package com.mpw.repetition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by wilk.wojtek@gmail.com.
 */
@RequestMapping("/api/repetition")
@RestController
public class RepetitionController {

    private final RepetitionService repetitionService;

    @Autowired
    public RepetitionController(RepetitionService repetitionService) {
        this.repetitionService = repetitionService;
    }

    @RequestMapping(value = "/next", method = RequestMethod.POST)
    public void scheduleNext(@RequestBody CardGradeTO grade){
        repetitionService.grade(grade.grade, grade.cardId);
        repetitionService.scheduleNext(grade.cardId);
    }

    @RequestMapping( value = "/planned/{date}", method = RequestMethod.GET)
    public long getPlanned(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ParseException {
        return repetitionService.countPlanned(date);
    }

    @RequestMapping
    public List<Repetition> findAll(){
        return repetitionService.findAll();
    }
}
