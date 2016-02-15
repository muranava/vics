
angular
  .module('canvass')
  .service('voteService', function(apiUrl, $http) {
    var api = {};

    api.vote = function(ern) {
      return $http.post(apiUrl + '/vote/' + ern);
    };

    return api;
  });
