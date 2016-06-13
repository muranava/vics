angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService, toastr) {

    statsService.constituenciesStats()
      .success(function(response) {
        $scope.constituencies = response.constituencies;
        $scope.totals = response.total;
        computePercentages();
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

    $scope.percentOf = function(pledges, pledgesVoted) {
      return _.parseInt(pledgesVoted / pledges * 100) + '%';
    };

    function computePercentages() {
      $scope.percentWithIntentions = _.parseInt($scope.totals.canvassed / $scope.totals.voters * 100);
      $scope.percentPledgesVoted = _.parseInt($scope.totals.pledgesVoted / $scope.totals.pledges * 100);
      $scope.percentVoted = _.parseInt($scope.totals.voted / $scope.totals.voters * 100);
    }

  });
