angular
  .module('canvass')
  .controller('gotvController', function ($scope, wardService, gotvService) {

    $scope.numStreetsSelected = 0;
    $scope.validationErrors = [];

    /**
     * Model for radio buttons (it should be easier than this.
     * I was able to almost render the UI dynamically from a model, but wan't able to
     * get it to work with default values, so I went back to the primitive version - time).
     */
    $scope.radios = [
      {
        name: 'wantsPV',
        enabled: false,
        value: null,
        handler: function (newValue) {
          this.value = newValue;
        }
      },
      {
        name: 'hasPV',
        enabled: false,
        value: null,
        handler: function (newValue) {
          this.value = newValue;
        }
      },
      {
        name: 'lift',
        enabled: false,
        value: null,
        handler: function (newValue) {
          this.value = newValue;
        }
      },
      {
        name: 'poster',
        enabled: false,
        value: null,
        handler: function (newValue) {
          this.value = newValue;
        }
      }
    ];

    function validateFlagsRadios() {
      var errors = [];

      $scope.radios.map(function (radio) {
        if (radio.enabled && radio.value === null) {
          errors.push("Radio For " + radio.name + " is not selected");
        }
      });

      return errors;
    }

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

    $scope.onLoadedConstituencies = function (constituencies) {
      if (_.isEmpty(constituencies)) {
        $scope.userHasNoAssociations = true;
      }
      $scope.contentLoaded = true;
    };

    $scope.onSelectWard = function (directiveModel) {
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

    $scope.onPrintSelected = function () {
      $scope.errorLoadingData = null;
      var selected = _.filter($scope.streets, function (s) {
        return s.selected;
      });
      doPrint($scope.ward.code, selected);
    };

    $scope.onPrintAll = function () {
      $scope.errorLoadingData = null;
      doPrint($scope.ward.code, $scope.streets);
    };

    function doPrint(wardCode, streets) {
      $scope.validationErrors = validateFlagsRadios();
      if (_.isEmpty($scope.validationErrors)) {
        var data = buildRequest(streets);
        gotvService.retrievePdfOfElectorsByStreets(wardCode, data)
          .success(function (response) {
            var file = new Blob([response], {type: 'application/pdf'});
            saveAs(file, $scope.ward.code + '.pdf');
          })
          .error(function(error) {
            console.log(error);
            $scope.errorLoadingData = error.message;
          });
      }
    }

    function buildRequest(streets) {
      var flags = getActiveOptionalFlags();
      flags.intentionFrom = $scope.intentionSlider.minValue;
      flags.intentionTo = $scope.intentionSlider.maxValue;
      flags.likelihoodFrom = $scope.likelihoodSlider.minValue;
      flags.likelihoodTo = $scope.likelihoodSlider.maxValue;
      return {
        streets: streets,
        flags: flags
      };
    }

    function getActiveOptionalFlags() {
      var flags = {};
      $scope.radios.map(function (radio) {
        if (radio.enabled && radio.value !== null) {
          flags[radio.name] = radio.value;
        }
      });
      return flags;
    }
  });
