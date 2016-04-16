'use strict';

(function(){
    var app = angular.module('alexaFactoryModule', ['angular-growl'])
        .factory('alexaFactory', function($rootScope, growl) {

        var socket = new SockJS('/stomp');
        var stompClient = Stomp.over(socket);

        function sendResponseEvent(responseEvent){
            growl.success(responseEvent.payload);
            stompClient.send("/app/alexaResponse", {}, JSON.stringify(responseEvent));
        };

        function dispatchRequest(alexaRequestEvent){
            $rootScope.$emit('alexaRequestEvent', alexaRequestEvent, responseCallback(alexaRequestEvent));
        };

        function responseCallback(alexaRequestEvent){
            return function(response){
                factory.sendResponse(alexaRequestEvent, response);
            };
        };

        var factory = {
            connectToAlexa: function(){
                stompClient.connect({ }, function(frame) {
                    stompClient.subscribe('/topic/alexaRequest', function(response) {
                        dispatchRequest(JSON.parse(response.body));
                    });
                });
            },
            sendResponse: function(alexaRequestEvent, response){
                var responseEvent = {
                    payload: response,
                    requestId: alexaRequestEvent.eventId
                }
                sendResponseEvent(responseEvent);
            }
        };

        return factory;
    });

})();