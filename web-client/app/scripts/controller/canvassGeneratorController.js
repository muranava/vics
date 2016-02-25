/**
 * Controller for the main canvass generator
 */
angular
  .module('canvass')
  .controller('canvassGeneratorController', function ($window, $scope, wardService, constituencyService, electorService) {

    $scope.wards = [];
    $scope.constituencySearchModel = '';
    $scope.wardSearchModel = '';
    $scope.numStreetsSelected = 0;
    $scope.errorLoadingData = false;

    $scope.onSelectConstituency = function(constituency) {
      $scope.constituencySearchModel = constituency;
    };

    $scope.onSelectWard = function (model) {
      $scope.wardSearchModel = model.ward;
      $scope.constituencySearchModel = model.constituency;

      wardService.findStreetsByWard($scope.wardSearchModel.code)
        .success(function (streets) {
          $scope.streets = streets;
        });
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
