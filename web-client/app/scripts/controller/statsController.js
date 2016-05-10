angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService) {

    statsService.userCounts()
      .success(function (stats) {
        $scope.usersByRegionStats = stats;
      });
  });
