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

    /**
     * Search wards by name (server performs a contains string search ignoring case)
     * @param {String} name - the ward name to search
     * @param {Number} limit - number of params to return
     */
    api.search = function(name, limit) {
      var paramsEncoded = $.param({
        limit: limit,
        name: name
      });

      return $http({
        method: 'GET',
        url: apiUrl + '/ward/search?' + paramsEncoded,
        withCredentials: true
      });
    };

    return api;
  });
