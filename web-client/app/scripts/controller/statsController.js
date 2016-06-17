angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService, toastr) {

    function loadStats() {
      statsService.constituenciesStats()
        .success(function(response) {
          $scope.constituencies = response.constituencies;
          $scope.totals = response.total;
          $scope.lastUpdated = new Date(response.updated);
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
    }

    $scope.percentOf = function(pledgesVoted, totalPledges) {
      if (totalPledges === 0) {
        return '100%';
      }
      return _.parseInt(pledgesVoted / totalPledges * 100) + '%';
    };

    $scope.refreshConstituencyStats = function() {
      loadStats();
    };

    function computePercentages() {
      $scope.percentWithIntentions = _.parseInt($scope.totals.canvassed / $scope.totals.voters * 100);
      $scope.percentPledgesVoted = _.parseInt($scope.totals.pledgesVoted / $scope.totals.pledges * 100);
      $scope.percentVoted = _.parseInt($scope.totals.voted / $scope.totals.voters * 100);
    }

    loadStats();
  });

