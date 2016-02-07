/**
 * Root controller of the application
 */
angular
    .module('canvass')
    .controller('parentController', function ($location, $scope, authService, $rootScope) {

      $scope.logout = function() {
        authService.logout()
          .finally(function() {
            $rootScope.currentUser = null;
            $location.path('/login');
          });
      };
    });
