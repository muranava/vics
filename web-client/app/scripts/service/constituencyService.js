
angular
  .module('canvass')
  .service('constituencyService', function (apiUrl, $http) {
    var api = {};

    api.retrieveByUser = function () {
      return $http({
        url: apiUrl + '/constituency',
        method: 'GET',
        withCredentials: true
      });
    };

    return api;
  });
