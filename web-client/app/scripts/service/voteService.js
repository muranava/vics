angular
  .module('canvass')
  .service('voteService', function (config, $http) {
    var api = {},
      apiUrl = config.apiUrl;

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
