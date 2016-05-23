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

    api.undoVote = function(model) {
      var fullErn = model.wardCode + '-' + model.ern;
      return $http({
        method: 'DELETE',
        url: apiUrl + '/elector/' + fullErn + '/voted',
        withCredentials: true
      });
    };

    return api;
  });
