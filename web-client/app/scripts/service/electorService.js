angular
  .module('canvass')
  .service('electorService', function (apiUrl, $http, util) {
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
    api.retrieveElectorsByStreets = function (wardCode, data) {
      return $http({
        url: apiUrl + '/elector/ward/' + wardCode + '/street',
        method: 'POST',
        withCredentials: true,
        data: {
          streets: data
        }
      });
    };

    api.retrieveElectorByErn = function(ern) {
      return $http({
        url: apiUrl + '/elector/' + ern,
        method: 'GET',
        withCredentials: true
      });
    };

    api.submitCanvassInput = function(inputModel) {
      return $http({
        url: apiUrl + '/elector/' + inputModel.ern + '/contact',
        method: 'POST',
        data: inputModel,
        withCredentials: true
      });
    };

    api.search = function(searchParams, limit) {
      var params = {};
      if (util.notEmpty(searchParams.firstName)) {
        params.firstName = searchParams.firstName;
      }
      if (util.notEmpty(searchParams.lastName)) {
        params.lastName = searchParams.lastName;
      }
      if (util.notEmpty(searchParams.address)) {
        params.address = searchParams.address;
      }
      if (util.notEmpty(searchParams.postCode)) {
        params.postCode = searchParams.postCode;
      }
      params.limit = limit;

      return $http({
        url: apiUrl + '/elector',
        method: 'GET',
        params: params,
        withCredentials: true
      });
    };

    /**
     * Retrieves the households and electors in the given streets
     */
    api.retrievePdfOfElectorsByStreets = function (wardCode, data) {
      return $http({
        url: apiUrl + '/elector/ward/' + wardCode + '/street/pdf',
        method: 'POST',
        responseType: 'arraybuffer',
        headers: {
          accept: 'application/pdf'
        },
        withCredentials: true,
        data: {
          streets: data
        }
      });
    };

    return api;
  });
