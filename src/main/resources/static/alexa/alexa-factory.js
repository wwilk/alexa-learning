'use strict';

(function(){
    var app = angular.module('alexaFactoryModule', ['angular-growl']).factory('alexaFactory', function($http, $q, $log, growl) {

        var factory = {
            notifyUser : function(message){
                growl.success(message);
            }
        };

        function handleResponse(response){
            growl.success(response.body);
        };

        function connectToAlexa(){
            var socket = new SockJS('/stomp');

            var stompClient = Stomp.over(socket);

            stompClient.connect({ }, function(frame) {
                // subscribe to the /topic/message endpoint
                stompClient.subscribe("/topic/message", function(data) {
                    handleResponse(data);
                });
            });

        };

        connectToAlexa();

        return factory;
    });

})();