
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
      electorID: '',
      name: '',
      address: '',
      likelihood: 3,
      intention: 3,
      issues: {
        cost: false,
        economy: false,
        sovereignty: false
      },
      notes: {
        hasPV: false,
        wantsPV: false,
        poster: false,
        dead: false
      }
    };

    $scope.onSearchVoter = function() {
      $scope.elector = null;
      electorService.retrieveElectorByErn($scope.inputRecordModel.electorID)
        .success(function(response) {
          $scope.elector = response;
          $scope.electorName = $scope.elector.lastName + ", " + $scope.elector.firstName;
          $scope.electorAddress = $scope.elector.address;
        })
        .error(function() {
          $scope.elector = null;
        });
    };
  });
