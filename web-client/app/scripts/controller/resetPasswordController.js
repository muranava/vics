
angular
  .module('canvass')
  .controller('resetPasswordController', function ($scope, resetPasswordService) {

    $scope.onResetPassword = function() {
      resetPasswordService.resetPassword($scope.username)
        .success(function(response) {
          
        })
        .error(function(error) {

        });

    };

  });
