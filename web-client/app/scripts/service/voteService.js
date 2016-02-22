angular
  .module('canvass')
  .service('voteService', function (apiUrl, $http) {
    var api = {};

    api.recordVote = function (ern) {
      return $http({
        method: 'POST',
        url: apiUrl + '/elector/' + ern + '/voted',
        withCredentials: true
      });
    };

    return api;
  });
