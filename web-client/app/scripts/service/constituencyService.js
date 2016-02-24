
angular
  .module('canvass')
  .service('constituencyService', function (config, $http) {
    var api = {},
      apiUrl = config.apiUrl;

    api.retrieveByUser = function () {
      return $http({
        url: apiUrl + '/constituency',
        method: 'GET',
        withCredentials: true
      });
    };

    /**
     * Search constituencies by name (server performs a contains string search ignoring case)
     * @param {String} name - the ward name to search
     * @param {Number} limit - number of params to return
     */
    api.search = function(name, limit) {
      var paramsEncoded = $.param({
        limit: limit,
        name: name
      });

      return $http({
        method: 'GET',
        url: apiUrl + '/constituency/search?' + paramsEncoded,
        withCredentials: true
      });
    };

    api.associateToUser = function(constituencyID, userID) {
      return $http({
        method: 'POST',
        url: apiUrl + '/constituency/' + constituencyID + '/user/' + userID,
        withCredentials: true
      });
    };

    api.removeUserAssociation = function(constituencyID, userID) {
      return $http({
        method: 'DELETE',
        url: apiUrl + '/constituency/' + constituencyID + '/user/' + userID,
        withCredentials: true
      });
    };

    return api;
  });
