/**
 * Reusable UI component that displays health bars
 */
angular
    .module('canvass')
    .directive('healthComponent', function(healthService) {
        return {
            restrict: 'EA',
            templateUrl: 'views/health-component.html',
            link: function(scope) {
                healthService
                    .retrieveOwnHealthStatus()
                    .success(function(response) {
                        scope.health = response;
                    });
            }
        };
    });
