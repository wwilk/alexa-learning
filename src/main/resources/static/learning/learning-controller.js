'use strict';

(function(){
    var app = angular.module('learningControllerModule', ['learningFactoryModule', 'alexaFactoryModule']);

    app.controller('learningController', function($scope, $rootScope, $location, learningFactory, alexaFactory) {

        var _this = this;
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
            if(this.answer === this.card.answer){
                message = "Correct!";
            } else{
                message = "Wrong!";
            }
            $rootScope.$emit('alexaResponseEvent', alexaRequestEvent.requestId, 'done, boy');
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

