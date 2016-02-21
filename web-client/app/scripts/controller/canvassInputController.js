/**
 * Controls the data input form to record a canvasser input
 */
angular
  .module('canvass')
  .controller('canvassInputController', function ($scope, electorService, RingBuffer) {
    var logSize = 5;
    $scope.issues = [''];

    $scope.elector = null;
    $scope.logs = RingBuffer.newInstance(logSize);

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
        electorService.submitCanvassInput($scope.inputRecordModel)
          .success(function (response) {
            $scope.elector = response;
            $scope.electorName = $scope.elector.lastName + ", " + $scope.elector.firstName;
            $scope.electorAddress = $scope.elector.address;

            $scope.logs.push({
              ern: err.custom,
              reason: '-',
              success: true
            });
          })
          .error(function (err) {
            if (err && err.type === 'NotFoundFailure') {
              $scope.logs.push({
                ern: err.custom,
                reason: 'Invalid roll number',
                success: false
              });
            } else {
              $scope.logs.push({
                ern: err.custom,
                reason: 'Failed to contact server',
                success: false
              });
            }
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
