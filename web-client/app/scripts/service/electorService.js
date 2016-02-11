angular
  .module('canvass')
  .service('electorService', function (apiUrl, $http) {
    var api = {};

    /**
     * Retrieves the elector records for the given ward
     * @param {String[]} wardCode
     * @return {Object[]}
     */
    api.retrieveLocalElectorsByWards = function (wardCode) {
      return $http({
        url: apiUrl + '/ward/' + wardCode + '/elector',
        method: 'GET',
        withCredentials: true
      });
    };

    /**
     * Retrieves the households and electors in the given streets
     */
    api.retrieveElectorsByStreets = function(data) {
      return $http({
        url: apiUrl + '/elector/street',
        method: 'POST',
        withCredentials: true,
        data: {
          streets: data
        }
      });
    };

    return api;
  });
