/**
 * Service to retrieve wards and constituencies
 */
angular
  .module('canvass')
  .service('wardService', function ($http, apiUrl) {
    var api = {};

    /**
     * Finds the wards within constituency
     * @param {String} name the constituency name
     */
    api.findWardsWithinConstituency = function (name) {
      return $http({
        method: 'GET',
        url: apiUrl + '/ward',
        params: {
          constituency: name
        },
        withCredentials: true,
        cache: true
      });
    };

    return api;
  });
