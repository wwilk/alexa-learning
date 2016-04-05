'use strict';

// main module of application
// navigation is handled here and all dependencies of the screen are injected

(function(){

    var app = angular.module('hackathonApp', ['learningControllerModule', 'cardsControllerModule', 'ngRoute']);

    app.config(function($routeProvider, $locationProvider) {
      $routeProvider
       .when('/', {
        redirectTo: '/menu'
      })
      .when('/menu',{
          templateUrl: 'menu/menu.html',
          controller: 'menuController',
          controllerAs : 'menuCtrl'
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
      .when('/failure', {
        templateUrl: 'failure.html'
      });
    });

})();