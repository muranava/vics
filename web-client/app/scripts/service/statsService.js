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

    api.canvassedByWeekAndWard = function(code) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/ward/' + code + '/weekly',
        withCredentials: true
      });
    };

    api.canvassedByWeekAndConstituency = function(code) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/constituency/' + code + '/weekly',
        withCredentials: true
      });
    };

    api.wardStats = function(wardCode) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/ward/' + wardCode,
        withCredentials: true
      });
    };

    api.constituencyStats = function(constituencyCode) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/constituency/' + constituencyCode,
        withCredentials: true
      });
    };

    return api;
  });
