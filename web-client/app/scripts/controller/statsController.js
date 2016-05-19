angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService) {

    statsService.userCounts()
      .success(function (stats) {
        $scope.usersByRegionStats = stats;
      });

    statsService.allStats()
      .success(function(stats) {
        $scope.stats = stats;
      });
  });
