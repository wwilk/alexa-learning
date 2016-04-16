'use strict';

// main module of application
// navigation and default configurations are handled here

(function(){

    var app = angular.module('hackathonApp', ['learningControllerModule', 'cardsControllerModule', 'ngRoute', 'alexaFactoryModule']);

    app.config(['growlProvider', function(growlProvider) {
        growlProvider.globalTimeToLive(5000);
    }]);

    app.controller('mainController', function($rootScope, $location, alexaFactory){
        alexaFactory.connectToAlexa();
        $rootScope.$on('alexaRequestEvent', function(event, alexaRequestEvent){
            var method = alexaRequestEvent.method;

            var destination = null;
            if(method.name === 'GoToLearningIntent'){
                destination = "learning"
            } else if(method.name === 'GoToCardsIntent'){
                destination = "cards";
            }

            if(destination){
                window.location.hash = "/" + destination;
                var response = "Switched to " + destination + " mode";
                alexaFactory.sendResponse(response);
            }
        });
    });

    app.config(function($routeProvider, $locationProvider) {
      $routeProvider
       .when('/', {
        redirectTo: '/learning'
      })
      .when('/learning',{
        templateUrl: 'learning/learning.html',
        controller: 'learningController',
        controllerAs : 'learningCtrl'
      })
      .when('/cards', {
        templateUrl: 'cards/cards.html',
        controller: 'cardsController',
        controllerAs: 'cardsCtrl'
      })
    });

})();