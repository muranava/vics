angular
  .module('canvass')
  .service('statsService', function ($http, config) {
    var api = {},
      apiUrl = config.apiUrl;

    api.allStats = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats',
        withCredentials: true,
        cache: true
      });
    };

    api.topCanvassers = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topcanvassers',
        withCredentials: true,
        cache: true
      });
    };

    api.topWards = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topwards',
        withCredentials: true,
        cache: true
      });
    };

    api.userCounts = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/users',
        withCredentials: true,
        cache: true
      });
    };

    api.topConstituencies = function () {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/topconstituencies',
        withCredentials: true,
        cache: true
      });
    };

    api.canvassedByWeekAndWard = function(code) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/ward/' + code + '/weekly',
        withCredentials: true,
        cache: true
      });
    };

    api.canvassedByWeekAndConstituency = function(code) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/constituency/' + code + '/weekly',
        withCredentials: true,
        cache: true
      });
    };

    api.wardStats = function(wardCode) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/ward/' + wardCode,
        withCredentials: true,
        cache: true
      });
    };

    api.adminStats = function() {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/admin',
        withCredentials: true,
        cache: false
      });
    };

    api.constituencyStats = function(constituencyCode) {
      return $http({
        method: 'GET',
        url: apiUrl + '/stats/constituency/' + constituencyCode,
        withCredentials: true,
        cache: true
      });
    };

    return api;
  });
