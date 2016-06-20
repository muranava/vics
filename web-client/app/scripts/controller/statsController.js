angular
  .module('canvass')
  .controller('statsController', function ($scope, statsService, toastr, DTOptionsBuilder, DTColumnBuilder, $q, $route) {

    function loadConstituencies() {
      var deferred = $q.defer();

      statsService.constituenciesStats()
        .success(function (response) {
          var result = {};

          result.constituencies = _.map(response.constituencies, function (c) {
            c.percentFor = percentOf(c.stats.voted.pledged, c.stats.voted.total);
            c.percentPlgVoted = percentOf(c.stats.voted.pledged, c.stats.pledged);
            return c;
          });
          $scope.totals = response.total;
          $scope.lastUpdated = new Date(response.updated);

          $scope.percentWithIntentions = _.parseInt($scope.totals.canvassed / $scope.totals.voters * 100);
          $scope.percentPledgesVoted = _.parseInt($scope.totals.pledgesVoted / $scope.totals.pledges * 100);
          $scope.percentVoted = _.parseInt($scope.totals.voted / $scope.totals.voters * 100);

          deferred.resolve(result.constituencies);
        })
        .error(function () {
          toastr.error('Failed to load constituency stats', 'Error');
          deferred.reject();
        });

      return deferred.promise;
    }

    function loadStats() {
      $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withFnPromise(loadConstituencies)
        .withOption('responsive', true)
        .withPaginationType('full_numbers')
        .withDisplayLength(25);

      $scope.dtColumns = [
        DTColumnBuilder.newColumn('stats.name').withTitle('Constituency').withClass('tbl-cell-hover'),
        DTColumnBuilder.newColumn('region').withTitle('Region').withClass('tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.intention.1').withTitle('1').withClass('tbl-cell-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.intention.2').withTitle('2').withClass('tbl-cell-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.intention.3').withTitle('3').withClass('tbl-cell-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.intention.4').withTitle('4').withClass('tbl-cell-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.intention.5').withTitle('5').withClass('tbl-cell-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.voted.total').withTitle('Voted').withClass('text-center tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.pledged').withTitle('Plg.').withClass('tbl-cell-alt-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('stats.voted.pledged').withTitle('Plg. Voted').withClass('tbl-cell-alt-shaded tbl-cell-hover'),
        DTColumnBuilder.newColumn('percentPlgVoted').withTitle('Plg. Turnout %').withClass('tbl-cell-alt-shaded tbl-cell-hover')
          .renderWith(function (data) {
            return data + '%';
          }),
        DTColumnBuilder.newColumn('percentFor').withTitle('Voted For %').withClass('tbl-cell-alt-shaded tbl-cell-hover')
          .renderWith(function (data) {
            if (data === 50 || data === 0) {
              return '<span>' + data + '%</span>';
            } else if (data > 50) {
              return '<span class="text-success">' + data + '%</span>';
            }
            return '<span class="text-danger">' + data + '%</span>';
          })
      ];

      statsService.userCounts()
        .success(function (stats) {
          $scope.usersByRegionStats = stats;
        });

      statsService.adminStats()
        .success(function (stats) {
          $scope.adminStats = stats;
        });
    }

    function percentOf(pledgesVoted, totalPledges) {
      if (totalPledges === 0) {
        return '100';
      }
      return _.parseInt(pledgesVoted / totalPledges * 100);
    }

    $scope.refreshConstituencyStats = function () {
      $route.reload();
    };

    loadStats();
  });

