'use strict';

(function(){
    var app = angular.module('cardsControllerModule', ['cardsFactoryModule', 'alexaFactoryModule']);

    app.controller('cardsController', function($scope, cardsFactory, alexaFactory) {

        var _this = this;
        this.answer = '';
        this.question = '';

        function save(){
            cardsFactory.save({question: this.question, answer : this.answer})
                .then(function(){
                    alexaFactory.notifyUser("Card saved");
                });
        };

        this.save = save;
    });

})();

