'use strict';

(function(){
    var app = angular.module('cardsControllerModule', ['cardsFactoryModule', 'alexaFactoryModule']);

    app.controller('cardsController', function($scope, $rootScope, cardsFactory, alexaFactory) {

        var self = this;
        self.answer = '';
        self.question = '';

        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent, responseCallback){
            var method = alexaRequestEvent.method;
            var parameters = method.parameters;
            if(method.name === 'PrepareCardIntent'){
                fillForm(parameters.question, parameters.answer, responseCallback);
            } else if(method.name === 'ConfirmCardIntent'){
                saveAnswer(responseCallback);
            }
        });

        function fillForm(question, answer, responseCallback){
            self.question = question;
            self.answer = answer;
            var response = "Please correct the inputs and say 'correct' when you are done";
            responseCallback(response);
        };

        function saveAnswer(responseCallback){
            cardsFactory.save({question: self.question, answer : self.answer})
                .then(function(){
                    responseCallback('Card saved');
                    self.answer = '';
                    self.question = '';
                });
        };

        self.save = saveAnswer;
    });

})();

