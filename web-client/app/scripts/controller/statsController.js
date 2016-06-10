angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService) {

    statsService.userCounts()
      .success(function (stats) {
        $scope.usersByRegionStats = stats;
      });

    statsService.adminStats()
      .success(function (stats) {
        $scope.adminStats = stats;
      });
  });
