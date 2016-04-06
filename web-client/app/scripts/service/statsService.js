angular
  .module('canvass')
  .service('statsService', function ($http, config) {
    var api = {},
      apiUrl = config.apiUrl;

    api.allStats = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats',
        withCredentials: true
      });
    };

    api.topCanvassers = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topcanvassers',
        withCredentials: true
      });
    };

    api.topWards = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topwards',
        withCredentials: true
      });
    };

    api.topConstituencies = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topconstituencies',
        withCredentials: true
      });
    };

    return api;
  });
