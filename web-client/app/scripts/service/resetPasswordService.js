
angular
  .module('canvass')
  .service('resetPasswordService', function ($http, config) {
    var api = {},
      apiUrl = config.apiUrl;

    api.resetPassword = function (username) {
      return $http({
        method: 'POST',
        url: apiUrl + '/user/resetpassword',
        data: {
          username: username
        }
      });
    };

    return api;
  });
