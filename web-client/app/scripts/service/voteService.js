angular
  .module('canvass')
  .service('voteService', function (apiUrl, $http) {
    var api = {};

    api.recordVote = function (ern) {
      return $http({
        method: 'POST',
        url: apiUrl + '/voted/' + ern,
        withCredentials: true
      });
    };

    return api;
  });
