package com.mpw.repetition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @RequestMapping(value = "/next")
    public void scheduleNext(@RequestBody GradeTO grade){
        repetitionService.grade(grade.grade, grade.cardId);
        repetitionService.scheduleNext(grade.cardId);
    }

    @RequestMapping( value = "/planned/{date}")
    public long getPlanned(@PathVariable String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return repetitionService.countPlanned(sdf.parse(date));
    }

    @RequestMapping
    public List<Repetition> findAll(){
        return repetitionService.findAll();
    }
}
