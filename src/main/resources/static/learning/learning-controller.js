'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'angular-growl']);

    app.controller('learningController', function($rootScope, $location, learningFactory, alexaFactory, growl) {

        var self = this;

        self.card = null;
        self.question = '';
        self.answer = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent, responseCallback){
            var method = alexaRequestEvent.method;
            if(method.name === 'CheckAnswerIntent'){
                checkAnswer(responseCallback);
            } else if(method.name === 'AssessmentIntent'){
                var grade = learningFactory.convertGrade(method.parameters.grade);
                gradeAnswer(grade, responseCallback);
            } else if(method.name === 'HowManyWordsIntent'){
                countPlanned(method.parameters.date, responseCallback);
            }
        });

        function checkAnswer(responseCallback){
            self.answer = self.card.answer;
            var response = 'The answer is ' + self.answer;
            responseCallback(response);
        };

        function gradeAnswer(grade, responseCallback){
            var response;
            if(grade === -1){
                response = 'Incorrect grade. The valid ones are excellent, good and bad';
            } else{
                var cardId = self.card.id;
                self.card = null;
                self.answer = '';
                self.question = '';
                learningFactory.gradeAnswer(grade, cardId).then(function(){
                    next();
                });
                response = 'Answer graded with ' + grade;
            }

            responseCallback(response);
        };

        function countPlanned(date, responseCallback){
            learningFactory.countPlanned(date).then(function(count){
                var response = "You have " + count + " words planned for " + learningFactory.formatDate(date);
                responseCallback(response);
            });
        };

        function next(){
            if(learningFactory.cards.length === 0){
                learningFactory.getPendingCards().then(function(cards){
                    if(cards.length === 0){
                        growl.error('Nothing more to learn');
                    } else{
                        next();
                    }
                });
            } else{
                self.card = learningFactory.cards.pop();
                self.answer = '';
                self.question = self.card.question;
            }
        };

        next();

        this.checkAnswer = checkAnswer;
    });

})();

