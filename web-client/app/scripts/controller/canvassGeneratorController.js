
/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($window, $scope, wardService, constituencyService, apiUrl, electorService) {

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
      $scope.wardSearchModel = null;
      $scope.wards = [];
      $scope.errorLoadingData = false;
      reloadWardsByConstituency();
    };

    function reloadWardsByConstituency() {
      $scope.errorLoadingData = false;
      wardService.findWardsWithinConstituency($scope.constituencySearchModel.id)
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

      if (!$scope.constituencySearchModel.id) {
        $scope.constituencyNotSelectedError = true;
      } else {
        if ($scope.wardSearchModel) {
          wardService.findStreetsByWard($scope.wardSearchModel.code)
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
      electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, selected)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.wardSearchModel.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = false;
      electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, $scope.streets)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.wardSearchModel.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };
  });
