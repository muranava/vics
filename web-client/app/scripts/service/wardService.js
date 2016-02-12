/**
 * Service to retrieve wards and constituencies
 */
angular
  .module('canvass')
  .service('wardService', function ($http, apiUrl) {
    var api = {};

    /**
     * Finds the wards within constituency
     * @param {String} id the constituency id
     */
    api.findWardsWithinConstituency = function (id) {
      return $http({
        method: 'GET',
        url: apiUrl + '/ward/constituency/' + id,
        withCredentials: true
      });
    };

    api.findStreetsByWard = function(wardCode) {
      return $http({
        method: 'GET',
        url: apiUrl + '/ward/' + wardCode + '/street',
        withCredentials: true
      });
    };

    /**
     * Finds all wards (the server will restrict the result set to
     * the wards the current user has access to)
     */
    api.findAll = function() {
      return $http({
        method: 'GET',
        url: apiUrl + '/ward',
        withCredentials: true
      });
    };

    return api;
  });
