
angular
  .module('canvass')
  .controller('adminUserController', function($timeout, $scope, userService, wardService) {


    userService.retrieveAllUsers()
      .success(function(response) {
        $scope.users = response;
      });
  });
