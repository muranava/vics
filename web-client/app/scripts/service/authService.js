angular
  .module('canvass')
  .factory("authService", function ($http, $q, $window, apiUrl) {
    var userInfo;

    function login() {
      var deferred = $q.defer();

      $http({
        url: apiUrl + '/user/login/test',
        method: 'GET',
        withCredentials: true,
        xsrfCookieName: 'SESSION'
      }).then(function (result) {
        deferred.resolve(userInfo);
      }, function (error) {
        deferred.reject(error);
      });

      return deferred.promise;
    }

    return {
      login: login
    };
  });
