
angular
  .module('canvass')
  .controller('gotvController', function($scope, wardService, electorService) {

    $scope.numStreetsSelected = 0;

    function defaultSliderOptions() {
      return {
        minValue: 3,
        maxValue: 4,
        options: {
          floor: 1,
          ceil: 5,
          showTicks: true,
          showTicksValues: true
        }
      };
    }
    $scope.intentionSlider = defaultSliderOptions();
    $scope.likelihoodSlider = defaultSliderOptions();

    $scope.onSearch = function () {
      wardService.findStreetsByWard($scope.ward.code)
        .success(function (streets) {
          $scope.streets = streets;
        });
    };

    $scope.onSelectWard = function(directiveModel) {
      $scope.ward = directiveModel.ward;
      $scope.constituency = directiveModel.constituency;
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
      electorService.retrievePdfOfElectorsByStreets($scope.ward.code, selected)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.ward.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = false;
      electorService.retrievePdfOfElectorsByStreets($scope.ward.code, $scope.streets)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.ward.code + '.pdf');
        })
        .error(function () {
          $scope.errorLoadingData = true;
        });
    };

  });
