/**
 * Service to perform authentication operations
 */
angular
  .module('canvass')
  .service("authService", function ($http, $q, $window, apiUrl) {
    var api = {};

    api.login = function (username, password) {
      return $http({
        url: apiUrl + '/user/login',
        method: 'POST',
        headers: {
          'Authorization': generateAuthHeader(username, password)
        },
        withCredentials: true,
        xsrfCookieName: 'SESSION'
      });
    };

    api.test = function (role) {
      role = role || 'ADMIN';
      return $http({
        url: apiUrl + '/user/login/test?role=' + role,
        method: 'GET',
        withCredentials: true
      });
    };

    function generateAuthHeader(username, password) {
      var base64 = btoa(username + ":" + password);
      return "Basic " + base64;
    }

    api.logout = function () {
      return $http({
        url: apiUrl + '/user/logout',
        method: 'POST',
        withCredentials: true
      });
    };

    return api;
  });
