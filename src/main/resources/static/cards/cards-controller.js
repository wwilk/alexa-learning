'use strict';

(function(){
    var app = angular.module('cardsControllerModule', ['cardsFactoryModule', 'alexaFactoryModule']);

    app.controller('cardsController', function($scope, $rootScope, cardsFactory, alexaFactory) {

        var self = this;
        self.answer = '';
        self.question = '';

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
            var response = "Please correct the inputs and say 'correct' when you are done";
            alexaFactory.sendResponse(response);
        };

        function saveAnswer(){
            cardsFactory.save({question: self.question, answer : self.answer})
                .then(function(){
                    alexaFactory.sendResponse('Card saved');
                    self.answer = '';
                    self.question = '';
                });
        };

        self.save = saveAnswer;
    });

})();

