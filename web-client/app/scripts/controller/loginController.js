angular
  .module('canvass')
  .controller('loginController', function ($scope, $http, $location, apiUrl) {

    $scope.credentials = {};

    $scope.login = function () {
      $http({
        url: apiUrl + '/user/login',
        method: 'POST',
        headers: {
          'Authorization': undefined
        },
        withCredentials: true,
        xsrfCookieName: 'SESSION'
      })
        .success(function (response) {
          console.log(response)
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

    $scope.logout = function () {
      $http({
        url: apiUrl + '/user/logout',
        method: 'POST',
        withCredentials: true,
        xsrfCookieName: 'SESSION'
      })
        .success(function (response) {
          console.log(response)
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
