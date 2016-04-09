'use strict';

(function(){
    var app = angular.module('cardsControllerModule', ['cardsFactoryModule', 'alexaFactoryModule']);

    app.controller('cardsController', function($scope, $rootScope, cardsFactory, alexaFactory) {

        var self = this;
        this.answer = '';
        this.question = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent){
            var method = alexaRequestEvent.method;
            if(method.name === 'PrepareCardIntent'){
                fillForm(method.parameters.question, method.parameters.answer);
            } else if(method.name === 'ConfirmCardIntent'){
                saveAnswer();
            }
        });

        function fillForm(question, answer){
            self.question = question;
            self.answer = answer;
            var message = "Please correct the inputs and say 'correct' when you are done";
            $rootScope.$emit('alexaResponseEvent', message);
        };

        function saveAnswer(){
            cardsFactory.save({question: self.question, answer : self.answer})
                .then(function(){
                    $rootScope.$emit('alexaResponseEvent', 'Card saved');
                });
        };

        this.save = save;
    });

})();

