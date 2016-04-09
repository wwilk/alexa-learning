'use strict';

(function(){
    var app = angular.module('learningFactoryModule', []).factory('learningFactory', function($http, $q, $log) {

        var daysOfMonth = [
                    "1st",
                    "2nd",
                    "3rd",
                    "4th",
                    "5th",
                    "6th",
                    "7th",
                    "8th",
                    "9th",
                    "10th",
                    "11th",
                    "12th",
                    "13th",
                    "14th",
                    "15th",
                    "16th",
                    "17th",
                    "18th",
                    "19th",
                    "20th",
                    "21st",
                    "22nd",
                    "23rd",
                    "24th",
                    "25th",
                    "26th",
                    "27th",
                    "28th",
                    "29th",
                    "30th",
                    "31st"
            ];

            var months = [
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
            ];



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
            },
            gradeAnswer: function(grade, cardId){
                var deferred = $q.defer();
                $http.post('api/repetition/next/', {
                        cardId : cardId, grade: grade
                    })
                   .success(function(data) {
                      deferred.resolve(data);
                   })
                   .error(onError(deferred));
                return deferred.promise;
            },
            countPlanned: function(date){
                var deferred = $q.defer();
                $http.get('api/repetition/planned/' + date)
                   .success(function(data) {
                      deferred.resolve(data);
                   })
                   .error(onError(deferred));
                return deferred.promise;
            },
            formatDate: function(date) {
                var parts = date.split("-");
                var year = parts[0];
                var month = parseInt(parts[1]);
                var day = parseInt(parts[2]);

                return months[month - 1] + " " + daysOfMonth[day - 1];
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