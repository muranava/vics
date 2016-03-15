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

    $scope.onSelectConstituency = function (constituency) {
      resetErrors();
      $scope.constituencySearchModel = constituency;
      $scope.wardSearchModel = '';
    };

    $scope.onLoadedConstituencies = function (constituencies) {
      if (_.isEmpty(constituencies)) {
        $scope.userHasNoAssociations = true;
      }
      $scope.contentLoaded = true;
    };

    $scope.onSelectWard = function (model) {
      resetErrors();

      $scope.wardSearchModel = model.ward;
      $scope.constituencySearchModel = model.constituency;

      wardService.findStreetsByWard($scope.wardSearchModel.code)
        .success(function (streets) {
          $scope.streets = streets;
        })
        .error(function () {
          $scope.errorLoadingStreets = true;
        });
    };

    $scope.onNoAssociations = function () {
      $scope.userHasNoAssociations = true;
    };

    $scope.getNumStreetsSelected = function () {
      if ($scope.streets && $scope.streets.length) {
        $scope.numStreetsSelected = _.size(_.filter($scope.streets, function (s) {
          return s.selected;
        }));
      }
    };

    $scope.onPrintSelected = function () {
      resetErrors();
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, {streets: selected})
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.wardSearchModel.code + '.pdf');
        })
        .error(function (error) {
          handleErrorResponse(error);
        });
    };

    function handleErrorResponse(error) {
      if (error) {
        $scope.errorLoadingData = error.message;
      } else {
        $scope.errorLoadingData = "Failed to contact server";
      }
    }

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = null;
      electorService.retrievePdfOfElectorsByStreets($scope.wardSearchModel.code, {streets: $scope.streets})
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.wardSearchModel.code + '.pdf');
        })
        .error(function (error) {
          handleErrorResponse(error);
        });
    };

    function resetErrors() {
      $scope.failedToLoadStreets = null;
      $scope.errorLoadingData = null;
    }
  });
