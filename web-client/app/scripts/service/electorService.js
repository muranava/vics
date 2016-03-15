angular
  .module('canvass')
  .service('electorService', function (config, $http, util) {
    var api = {}, apiUrl = config.apiUrl;

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

    api.retrieveElectorByErn = function (ern) {
      return $http({
        url: apiUrl + '/elector/' + ern,
        method: 'GET',
        withCredentials: true
      });
    };

    api.submitCanvassInput = function (inputModel) {
      return $http({
        url: apiUrl + '/elector/' + inputModel.ern + '/contact',
        method: 'POST',
        data: inputModel,
        withCredentials: true
      });
    };

    api.undoCanvassInput = function (inputModel) {
      return $http({
        url: apiUrl + '/elector/' + inputModel.ern + '/contact/' + inputModel.contactId,
        method: 'DELETE',
        data: inputModel,
        withCredentials: true
      });
    };

    api.search = function (searchParams, limit) {
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
            accept: 'application/*'
          },
          withCredentials: true,
          data: data,
          transformResponse: function(data, header, status) {
            if (status === 404) {
              return {
                status: 404,
                message: 'No voters for streets'
              };
            }
            return data;
          }
        }
      );
    };

    return api;
  });
