angular
  .module('canvass')
  .service('userService', function ($http, apiUrl) {
    var api = {};

    api.retrieveAllUsers = function() {
      return $http({
        url: apiUrl + '/user',
        method: 'GET',
        withCredentials: true
      });
    };

    api.retrieveCurrentUser = function() {
      return $http({
        url: apiUrl + '/user/current',
        method: 'GET',
        withCredentials: true
      });
    };

    return api;
  });
