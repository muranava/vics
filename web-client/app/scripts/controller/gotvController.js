
angular
  .module('canvass')
  .controller('gotvController', function($scope, wardService, gotvService) {

    $scope.numStreetsSelected = 0;

    $scope.toggleFilters = [
      {label: 'Wants PV', modelName: 'wantsPV', enabled: false, isYes: false, isNo: false},
      {label: 'Has PV', modelName: 'wantsPV', enabled: false, isYes: false, isNo: false},
      {label: 'Needs Lift', modelName: 'lift', enabled: false, isYes: false, isNo: false},
      {label: 'Inaccessible', modelName: 'inaccessible', enabled: false, isYes: false, isNo: false},
      {label: 'Dead', modelName: 'dead', enabled: false, isYes: false, isNo: false}
    ];

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

    $scope.onLoadedConstituencies = function(constituencies) {
      if (_.isEmpty(constituencies)) {
        $scope.userHasNoAssociations = true;
      }
      $scope.contentLoaded = true;
    };

    $scope.onSelectWard = function(directiveModel) {
      $scope.ward = directiveModel.ward;
      $scope.constituency = directiveModel.constituency;

      wardService.findStreetsByWard($scope.ward.code)
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

    function handleDownloadPdfError() {
      $scope.errorLoadingData = true;
    }

    $scope.onPrintSelected = function () {
      $scope.errorLoadingData = false;
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      doPrint($scope.ward.code, selected);
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = false;
      doPrint($scope.ward.code, $scope.streets);
    };

    function doPrint(wardCode, streets) {
      var data = buildRequest(streets);
      gotvService.retrievePdfOfElectorsByStreets(wardCode, data)
        .success(function (response) {
          var file = new Blob([response], {type: 'application/pdf'});
          saveAs(file, $scope.ward.code + '.pdf');
        })
        .error(handleDownloadPdfError);
    }

    function buildRequest(streets) {
      return {
        streets: streets,
        flags: {
          intentionFrom: $scope.intentionSlider.minValue,
          intentionTo: $scope.intentionSlider.maxValue,
          likelihoodFrom: $scope.likelihoodSlider.minValue,
          likelihoodTo: $scope.likelihoodSlider.maxValue,
          hasPV: false,
          wantsPV: false
        }
      };
    }
  });
