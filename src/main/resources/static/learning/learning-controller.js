'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'angular-growl']);

    app.controller('learningController', function($scope, $rootScope, $location, learningFactory, growl) {

        var self = this;
        this.card = null;
        this.answer = '';
        this.question = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent){
            var method = alexaRequestEvent.method;
            if(method.name === 'CheckAnswer'){
                checkAnswer();
            } else if(method.name === 'Assessment'){
                gradeAnswer(method.parameters['grade']);
            }
        });

        function checkAnswer(){
            var message;
            if(self.answer === self.card.answer){
                message = "Correct!";
            } else{
                message = "Wrong!";
            }
            $rootScope.$emit('alexaResponseEvent', {message : message});
        };

        function gradeAnswer(grade){
            learningFactory.gradeAnswer(grade, self.card.id).then(function(){
                next();
            });
            $rootScope.$emit('alexaResponseEvent', {message : 'Answer graded with ' + grade});
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

