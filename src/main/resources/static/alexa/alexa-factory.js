'use strict';

(function(){
    var app = angular.module('alexaFactoryModule', ['angular-growl'])
        .factory('alexaFactory', function($http, $rootScope, $q, $log, growl) {

        var queueName = "/topic/alexaRequest";
        var socket = new SockJS('/stomp');
        var stompClient = Stomp.over(socket);

        var factory = {
            notifyUser : function(message){
                growl.success(message);
            }
        };

        $rootScope.$on('alexaResponseEvent', function(event, requestId, message){
            sendResponseToAlexa({requestId : requestId, message: message});
        });

        function dispatchResponse(alexaRequestEvent){
            //sendToAlexa(alexaRequestEvent);
            //growl.success(alexaRequestEvent);
            $rootScope.$emit('alexaRequestEvent', alexaRequestEvent);
        };

        function connectToAlexa(){
            stompClient.connect({ }, function(frame) {
                // subscribe to the /topic/message endpoint
                stompClient.subscribe(queueName, function(response) {
                    dispatchResponse(JSON.parse(response.body));
                });
            });
        };

        function sendResponseToAlexa(message){
            stompClient.send("/app/alexaResponse", {}, message);
        };

        connectToAlexa();

        return factory;
    });

})();