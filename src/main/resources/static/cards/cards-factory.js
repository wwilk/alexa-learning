'use strict';

(function(){
    var app = angular.module('cardsFactoryModule', []).factory('cardsFactory', function($http, $q, $log) {

        var factory = {
            save: function(card){
                var deferred = $q.defer();
                $http.post('api/cards/', card)
                   .success(function(data) {
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