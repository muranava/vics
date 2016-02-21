/**
 * Controls the data input form to record a canvasser input
 */
angular
  .module('canvass')
  .controller('canvassInputController', function ($scope, electorService) {
    $scope.issues = [''];

    $scope.elector = null;

    // main form data model
    $scope.inputRecordModel = {
      ern: '',
      likelihood: 3,
      intention: 3,
      cost: false,
      border: false,
      sovereignty: false,
      hasPV: false,
      wantsPV: false,
      poster: false,
      lift: false,
      deceased: false
    };

    $scope.onSubmitRecord = function () {
      $scope.errors = validateForm();
      if (_.isEmpty($scope.errors)) {
        console.log($scope.inputRecordModel);
        electorService.submitCanvassInput($scope.inputRecordModel)
          .success(function (response) {
            $scope.elector = response;
            $scope.electorName = $scope.elector.lastName + ", " + $scope.elector.firstName;
            $scope.electorAddress = $scope.elector.address;
          })
          .error(function () {
            $scope.elector = null;
          });
      }
    };

    function validateForm() {
      var errors = [];

      if (_.isEmpty($scope.inputRecordModel.ern)) {
        errors.push("Elector ID is empty");
      }

      return errors;
    }

    $scope.onSearchVoter = function () {
      $scope.elector = null;
      electorService.retrieveElectorByErn($scope.inputRecordModel.ern)
        .success(function (response) {
          $scope.elector = response;
          $scope.electorName = $scope.elector.lastName + ", " + $scope.elector.firstName;
          $scope.electorAddress = $scope.elector.address;
        })
        .error(function () {
          $scope.elector = null;
        });
    };
  });
