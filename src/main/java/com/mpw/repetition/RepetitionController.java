package com.mpw.repetition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping
    public List<Repetition> findAll(){
        return repetitionService.findAll();
    }
}
