
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($window, $scope, wardService, constituencyService, apiUrl, electorService, electorTable, $filter) {

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';
    $scope.numStreetsSelected = 0;
    $scope.errorLoadingData = false;

    $scope.loadingConstituencies = true;
    constituencyService.retrieveByUser()
      .success(function (response) {
        $scope.constituencies = response.constituencies;
      })
      .error(function () {
        $scope.errorLoadingData = true;
      });

    $scope.onSelectConstituency = function () {
      $scope.wardSearchModel = "";
      $scope.errorLoadingData = false;
      $scope.selectedConstituency = _.find($scope.constituencies, {name: $scope.constituencySearchModel});
      if (!$scope.selectedConstituency) {
        $scope.wards = [];
      } else {
        reloadWardsByConstituency();
      }
    };

    function reloadWardsByConstituency() {
      $scope.errorLoadingData = false;
      wardService.findWardsWithinConstituency($scope.selectedConstituency.id)
        .success(function (response) {
          $scope.wards = response.wards;
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    }

    $scope.onSearch = function () {
      $scope.errorLoadingData = false;
      $scope.streets = [];
      $scope.wardNotSelectedError = false;
      $scope.constituencyNotSelectedError = false;
      $scope.selectedWard = _.find($scope.wards, {name: $scope.wardSearchModel});
      $scope.selectedConstituency = _.find($scope.constituencies, {name: $scope.constituencySearchModel});
      if (!$scope.selectedConstituency) {
        $scope.constituencyNotSelectedError = true;
      } else {
        if ($scope.selectedWard) {
          wardService.findStreetsByWard($scope.selectedWard.code)
            .success(function (streets) {
              $scope.streets = streets;
            })
            .error(function () {
              $scope.errorLoadingData = true;
            });
        } else {
          $scope.wardNotSelectedError = true;
        }
      }
    };

    $scope.getNumStreetsSelected = function () {
      if ($scope.streets && $scope.streets.length) {
        $scope.numStreetsSelected = _.size(_.filter($scope.streets, function (s) {
          return s.selected;
        }));
      }
    };

    $scope.onPrintSelected = function () {
      $scope.errorLoadingData = false;
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      electorService.retrievePdfOfElectorsByStreets($scope.selectedWard.code, selected)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.selectedWard.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = false;
      electorService.retrievePdfOfElectorsByStreets($scope.selectedWard.code, $scope.streets)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.selectedWard.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };
  });
