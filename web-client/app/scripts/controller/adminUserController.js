
angular
  .module('canvass')
  .controller('adminUserController', function($timeout, $scope, userService, wardService) {
    wardService.findAll()
      .success(function(response) {
        $scope.allWards = response.wards;
        $timeout(function() {
          $('#constituenciesMulti').multiSelect({
            classes : "multi-select"
          });
        }, 100);
      });

    userService.retrieveAllUsers()
      .success(function(response) {
        $scope.users = response;
      });
  });
