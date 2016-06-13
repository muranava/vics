angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService, toastr) {

    statsService.constituenciesStats()
      .success(function(response) {
        $scope.constituencies = response;
      })
      .error(function() {
        toastr.error('Failed to load constituency stats', 'Error');
      });

    statsService.userCounts()
      .success(function (stats) {
        $scope.usersByRegionStats = stats;
      });

    statsService.adminStats()
      .success(function (stats) {
        $scope.adminStats = stats;
      });
  });
