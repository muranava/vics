angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, apiUrl, authService) {

    $scope.failedLogin = false;
    $scope.credentials = {};

    $scope.login = function () {
      $scope.failedLogin = false;
      authService.login($scope.credentials.username, $scope.credentials.password)
        .success(function (response) {
          $http({
            url: apiUrl + '/user/login/test',
            method: 'GET',
            withCredentials: true
          })
            .success(function (d) {
              $location.path('/dashboard');
            });
        })
        .error(function (err) {
          $scope.failedLogin = true;
        });
    };

    $scope.logout = function () {
        authService.logout()
        .success(function (response) {
          console.log(response);
          $http({
            url: apiUrl + '/user/login/test',
            method: 'GET',
            withCredentials: true
          })
            .success(function (d) {
              console.log(d)
            })
        })
        .error(function (err) {
          console.error(err);
        });
    };
  });
