angular
  .module('canvass')
  .service('voteService', function (apiUrl, $http) {
    var api = {};

    api.recordVote = function (model) {
      return $http({
        method: 'POST',
        url: apiUrl + '/elector/voted',
        data: model,
        withCredentials: true
      });
    };

    return api;
  });
