'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'angular-growl']);

    app.controller('learningController', function($scope, $rootScope, $location, learningFactory, growl) {

        var self = this;
        this.card = null;
        this.answer = '';
        this.question = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent){
            if(alexaRequestEvent.method.name = 'checkAnswer'){
                checkAnswer();
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

