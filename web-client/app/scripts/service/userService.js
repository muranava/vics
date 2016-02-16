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

    api.create = function(user) {
      return $http({
        url: apiUrl + '/user',
        method: 'POST',
        withCredentials: true,
        data: user
      });
    };

    api.delete = function(userID) {
      return $http({
        url: apiUrl + '/user/' + userID,
        method: 'DELETE',
        withCredentials: true
      });
    };

    return api;
  });
