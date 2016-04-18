angular
  .module('canvass')
  .service('geoService', function (config, $http) {
    var api = {}, apiUrl = config.apiUrl;

    api.reverseLookupAddresses = function (addresses) {
      return $http({
        method: 'POST',
        url: apiUrl + '/geo/addresslookup',
        data: {addresses: addresses},
        withCredentials: true
      });
    };

    api.findWardFromPostCode = function (postCode) {
      return $http({
        method: 'GET',
        url: 'https://mapit.mysociety.org/postcode/' + postCode
      });
    };

    api.findPostCodeFromCoordinates = function(lat, lng) {
      return $http({
        method: 'GET',
        url: 'http://postcodes.io/postcodes?lat=' + lat + '&lon=' + lng
      });
    };

    return api;
  });
