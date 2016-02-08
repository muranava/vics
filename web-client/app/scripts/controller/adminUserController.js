
angular
  .module('canvass')
  .controller('adminUserController', function($scope, userService) {
    userService.retrieveAllUsers()
      .success(function(response) {
        $scope.users = response;
      });
  });
