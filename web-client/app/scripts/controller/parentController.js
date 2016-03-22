/**
 * Root controller of the application
 */
angular
  .module('canvass')
  .controller('parentController', function ($location, $scope, authService, $rootScope) {
    $rootScope.dropdownOpen = false;

    function resetMenu() {
      // Added to enforce menus are removed on logout
      if (!$scope.$$phase) {
        $scope.$apply();
      }
    }

    resetMenu();

    $scope.logout = function () {
      authService.logout()
        .finally(function () {
          $location.path('/login');
          _.defer(function () {
            resetMenu();
          });
        });
      $rootScope.currentUser = null;
    };

  });
