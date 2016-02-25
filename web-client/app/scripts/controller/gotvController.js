
angular
  .module('canvass')
  .controller('gotvController', function($scope) {

    $scope.onSelectWard = function(ward) {
      $scope.ward = ward;
    };

  });
