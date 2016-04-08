angular
  .module('canvass')
  .service('geoService', function (config, $http) {
    var api = {}, apiUrl = config.apiUrl;

    api.reverseLookupAddress = function (searchTerm) {
      return $http({
        method: 'GET',
        url: apiUrl + '/geo/addresslookup',
        params: {q: searchTerm},
        withCredentials: true
      });
    };

    api.findWardFromPostCode = function (postCode) {
      return $http({
        method: 'GET',
        url: 'https://mapit.mysociety.org/postcode/' + postCode
      });
    };

    return api;
  });
