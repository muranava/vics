/**
 * Controls the data input form to record a canvasser input
 */
angular
  .module('canvass')
  .controller('canvassInputController', function ($scope, electorService, RingBuffer, wardService) {
    var logSize = 5,
      searchLimit = 10,
      wardSearchLimit = 50;

    $scope.issues = [''];
    $scope.searchResults = [];
    $scope.elector = null;
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.inputRecordModel = {
      ern: '',
      likelihood: '3 - Undecided',
      intention: '3 - Undecided',
      cost: false,
      border: false,
      sovereignty: false,
      hasPV: false,
      wantsPV: false,
      poster: false,
      lift: false,
      deceased: false
    };

    $scope.searchForm = {
      firstName: '',
      lastName: '',
      address: '',
      postCode: ''
    };

    $scope.onWardInputKeypress = function() {
        $scope.invalidWard = false;
        wardService.searchRestricted($scope.wardSearchModel, wardSearchLimit)
          .success(function(response) {
            $scope.wards = response.wards;
          });
    };

    $scope.onSearchVoter = function() {
      function handleSuccess() {
        $scope.searchResults.push({

        });
      }

      function handleError() {
        $scope.searchFailed = true;
      }

      electorService.search($scope.searchForm, searchLimit)
        .success(handleSuccess)
        .error(handleError);
    };

    /**
     * Submits the entry form and adds the result to the log.
     */
    $scope.onSubmitRecord = function () {
      $scope.errors = validateForm();

      if (_.isEmpty($scope.errors)) {
        electorService.submitCanvassInput(mapFormToRequest($scope.inputRecordModel))
          .success(handleSubmitEntrySuccess)
          .error(handleSubmitEntryFailure);
      }
    };

    function mapFormToRequest(formModel) {
      var copy = $.extend(true, {}, formModel);
      copy.intention = _.parseInt(formModel.intention.charAt(0));
      copy.likelihood = _.parseInt(formModel.likelihood.charAt(0));
      return copy;
    }

    function handleSubmitEntrySuccess(response) {
      $scope.elector = response;
      $scope.electorName = $scope.elector.lastName + ", " + $scope.elector.firstName;
      $scope.electorAddress = $scope.elector.address;

      $scope.logs.push({
        ern: response.ern,
        reason: '-',
        success: true
      });
    }

    function handleSubmitEntryFailure(err) {
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
    }

    function validateForm() {
      var errors = [];

      if (_.isEmpty($scope.inputRecordModel.ern)) {
        errors.push("Elector ID is empty");
      }

      return errors;
    }
  });
