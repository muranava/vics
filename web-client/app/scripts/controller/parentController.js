/**
 * Root controller of the application
 */
angular
    .module('canvass')
    .controller('parentController', function ($location, $scope, authService, $rootScope) {

      // todo remove this.  Added to enforce menus are removed on logout
      if(!$scope.$$phase) {
        $scope.$apply();
      }

      $scope.logout = function() {
        authService.logout()
          .finally(function() {
            $location.path('/login');
          });
        $rootScope.currentUser = null;
      };
    });
