angular
  .module('canvass')
  .service('electorService', function (apiUrl, $http) {
    var api = {};

    /**
     * Retrieves a preview of the voters by the given constituency
     * and wards
     * @param {String} constituencyName
     * @param {String[]} wardNames
     * @return {Object}
     */
    api.previewVotersByWards = function (constituencyName, wardNames) {
      return $http({
        url: apiUrl + '/elector/local',
        method: 'POST',
        data: {
          wardCodes: wardNames
        },
        withCredentials: true
      });
    };

    /**
     * Retrieves the elector records for the given
     * @param {String[]} wardCodes
     * @return {Object[]}
     */
    api.retrievePafElectorsByWards = function (wardCodes) {
      return $http({
        method: 'POST',
        url: apiUrl + '/elector',
        data: {
          wardCodes: wardCodes
        },
        withCredentials: true
      });
    };

    /**
     * Retrieves the elector records for the given
     * @param {String[]} wardCodes
     * @return {Object[]}
     */
    api.retrieveLocalElectorsByWards = function (wardCodes) {
      return $http({
        url: apiUrl + '/elector/local',
        method: 'POST',
        data: {
          wardCodes: wardCodes
        },
        withCredentials: true
      });
    };

    return api;
  });
