/**
 * Root controller of the application
 */
angular
  .module('canvass')
  .controller('parentController', function ($location, $scope, authService, $rootScope) {
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
            $rootScope.currentUser = null;
          });
        });
    };

    // set the margins depending on the page type
    var fullPageRoutes = ['/login', '/resetpassword', '/newpassword'];
    $scope.$on('$routeChangeStart', function () {
      var currentRoute = $location.path(),
        isFullPage = false;
      _.forEach(fullPageRoutes, function (fullPageRoute) {
        if (_.startsWith(currentRoute, fullPageRoute)) {
          isFullPage = true;
        }
      });

      if (isFullPage) {
        $('#base').attr('class', 'fullPageMargins');
      } else {
        $('#base').removeClass('fullPageMargins');
      }
      resetMenu();
    });
  });
