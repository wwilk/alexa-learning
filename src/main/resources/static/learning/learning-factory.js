'use strict';

(function(){
    var app = angular.module('learningFactoryModule', []).factory('learningFactory', function($http, $q, $log) {

        var factory = {
            cards : [],
            getPendingCards: function(){
                var deferred = $q.defer();
                $http.get('api/cards/pending/')
                   .success(function(data) {
                      factory.cards = data;
                      deferred.resolve(data);
                   })
                   .error(onError(deferred));
                return deferred.promise;
            }
        };

        function onError(deferred){
            return function(msg, code){
                deferred.reject(msg);
                $log.error(msg, code);
            };
        };

        return factory;
    });

})();