'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'alexaFactoryModule']);

    app.controller('learningController', function($scope, $location, learningFactory, alexaFactory) {

        var _this = this;
        this.card = null;
        this.answer = '';
        this.question = '';

        function checkAnswer(){
            if(this.answer === this.card.answer){
                alexaFactory.notifyUser("Correct!");
            } else{
                alexaFactory.notifyUser("Wrong!");
            }
        };

        this.checkAnswer = checkAnswer;

        function next(){
            if(learningFactory.cards.length === 0){
                learningFactory.getPendingCards()
                    .then(function(cards){
                        if(cards.length === 0){

                        } else{
                            next();
                        }
                    });
            } else{
                _this.card = learningFactory.cards.pop();
                _this.answer = '';
                _this.question = _this.card.question;
            }
        };

        learningFactory.getPendingCards()
            .then(function(cards){
                next();
            });
    });

})();

