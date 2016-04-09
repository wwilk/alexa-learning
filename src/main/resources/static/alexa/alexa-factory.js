'use strict';

(function(){
    var app = angular.module('alexaFactoryModule', ['angular-growl'])
        .factory('alexaFactory', function($http, $rootScope, $q, $log, growl) {

        var socket = new SockJS('/stomp');
        var stompClient = Stomp.over(socket);
        var currentRequestEvent = null;

        $rootScope.$on('alexaResponseEvent', function(event, message){
            if(currentRequestEvent){
                var responseEvent = {
                    payload: message,
                    requestId: currentRequestEvent.eventId
                }
                sendResponseToAlexa(responseEvent);
            }
        });

        function sendResponseToAlexa(responseEvent){
            growl.success(responseEvent.payload);
            currentRequestEvent = null;
            stompClient.send("/app/alexaResponse", {}, JSON.stringify(responseEvent));
        };

        function dispatchRequest(alexaRequestEvent){
            currentRequestEvent = alexaRequestEvent;
            $rootScope.$emit('alexaRequestEvent', alexaRequestEvent);
        };

        var factory = {
            connectToAlexa: function(){
                stompClient.connect({ }, function(frame) {
                    stompClient.subscribe('/topic/alexaRequest', function(response) {
                        dispatchRequest(JSON.parse(response.body));
                    });
                });
            }
        };

        return factory;
    });

})();