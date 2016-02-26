
angular
  .module('canvass')
  .service('gotvService', function(config, $http) {
    var api = {}, apiUrl = config.apiUrl;

    /**
     * Retrieves the households and electors in the given streets
     */
    api.retrievePdfOfElectorsByStreets = function (wardCode, data) {
      return $http({
        url: apiUrl + '/gotv/ward/' + wardCode + '/street/pdf',
        method: 'POST',
        responseType: 'arraybuffer',
        headers: {
          accept: 'application/pdf'
        },
        withCredentials: true,
        data: data
      });
    };

    return api;
  });
