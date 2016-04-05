'use strict';

// validation directives

(function(){
    var directives = angular.module('userDirectivesModule', ['userFactoryModule']);

    directives.directive('uniqueUsername', function($q, userFactory) {
      return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
          ctrl.$asyncValidators.uniqueUsername = function(modelValue, viewValue) {

            if (ctrl.$isEmpty(modelValue)) {
              // empty username is validated elsewhere anyway
              return $q.when();
            }

            var def = $q.defer();

            userFactory.isUsernameAlreadyRegistered(modelValue)
                .then(function(response){
                    if (response === false) {
                        def.resolve(); // username is available
                    } else {
                        def.reject();
                    }
                });
            return def.promise;
          };
        }
      };
    });

    directives.directive("architechPassword", function() {
       return {
          require: "ngModel",
          link: function(scope, element, attrs, ctrl) {
            ctrl.$validators.architechPassword = function(modelValue, viewValue) {
                    if (ctrl.$isEmpty(modelValue)) {
                      // consider empty models to be valid
                      return true;
                    }

                    if (!hasAnyDigit(viewValue)) {
                      return false; // invalid
                    }

                    if(!hasAnyUpperCase(viewValue)){
                        return false;
                    }

                    if(!hasAnyLowerCase(viewValue)){
                        return false;
                    }

                    return true; // valid
                  };
         }
       };
    });

    function hasAnyDigit(str){
        return str.match(/\d/);
    };

    function hasAnyUpperCase(str){
        return str.toLowerCase() != str;
    };

    function hasAnyLowerCase(str){
        return str.toUpperCase() != str;
    };
})();