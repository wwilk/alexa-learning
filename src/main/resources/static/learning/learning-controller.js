'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'angular-growl']);

    app.controller('learningController', function($scope, $rootScope, $location, learningFactory, growl) {

        var self = this;
        this.card = null;
        this.question = '';
        this.answer = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent){
            var method = alexaRequestEvent.method;
            if(method.name === 'CheckAnswerIntent'){
                checkAnswer();
            } else if(method.name === 'AssessmentIntent'){
                var grade = convertGrade(method.parameters.grade);
                gradeAnswer(grade);
            } else if(method.name === 'HowManyWordsIntent'){
                countPlanned(method.parameters.date);
            }
        });

        function checkAnswer(){
            self.answer = self.card.answer;
            var message = "The answer is " + self.card.answer;
            $rootScope.$emit('alexaResponseEvent', message);
        };

        function gradeAnswer(grade){
            learningFactory.gradeAnswer(grade, self.card.id).then(function(){
                next();
            });
            self.answer = '';
            self.question = '';
            $rootScope.$emit('alexaResponseEvent', 'Answer graded with ' + grade);
        };

        function countPlanned(date){
            learningFactory.countPlanned(date).then(function(count){
                var message = "You have " + count + " words planned for " + learningFactory.formatDate(date);
                $rootScope.$emit('alexaResponseEvent', message);
            })

        }

        function convertGrade(alexaGrade){
            if(alexaGrade === 'good'){
                return 3;
            } else if(alexaGrade === 'excellent'){
                return 5;
            } else if(alexaGrade === 'bad'){
                return 1;
            }
            return -1;
        };

        this.checkAnswer = checkAnswer;

        function next(){
            if(learningFactory.cards.length === 0){
                learningFactory.getPendingCards()
                    .then(function(cards){
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

        learningFactory.getPendingCards()
            .then(function(cards){
                if(cards.length > 0){
                    next();
                } else{
                    growl.error('Nothing more to learn');
                }
            });
    });

})();

