angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, authService) {

    $scope.failedLogin = false;
    $scope.credentials = {};

    $scope.login = function () {
      $scope.badCredentials = false;
      $scope.networkError = false;
      authService.login($scope.credentials.username, $scope.credentials.password)
        .success(function () {
          $location.path('/dashboard');
        })
        .error(function (err) {
          if (err && err.type === 'LoginFailure') {
            $scope.badCredentials = true;
          } else {
            $scope.networkError = true;
          }
        });
    };

    $scope.logout = function () {
      authService.logout();
    };
  });
