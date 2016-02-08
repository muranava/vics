angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, apiUrl, authService) {

    $scope.failedLogin = false;
    $scope.credentials = {};

    $scope.login = function () {
      $scope.failedLogin = false;
      authService.login($scope.credentials.username, $scope.credentials.password)
        .success(function () {
          $location.path('/dashboard');
        })
        .error(function () {
          $scope.failedLogin = true;
        });
    };

    $scope.logout = function () {
      authService.logout();
    };
  });
