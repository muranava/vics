
angular
  .module('canvass')
  .service('constituencies', function ($http, apiUrl) {
    var api = {};

    api.findAllConstituencies = function() {
      return $http({
        method: 'GET',
        url: apiUrl + '/ward/constituency',
        withCredentials: true,
        cache: true
      });
    };

    return api;
  });
