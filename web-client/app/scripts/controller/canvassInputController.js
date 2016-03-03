/**
 * Controls the data input form to record a canvasser input
 */
angular
  .module('canvass')
  .controller('canvassInputController', function (util, $scope, electorService, RingBuffer, wardService) {
    var logSize = 5,
      searchLimit = 10;

    $scope.issues = [''];
    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.searchForm = {
      firstName: '',
      lastName: '',
      address: '',
      postCode: ''
    };

    function initForm() {
      var prevWard = $scope.inputRecordModel && $scope.inputRecordModel.ward;
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
        deceased: false,
        inaccessible: false,
        ward: null
      };

      if (prevWard) {
        $scope.inputRecordModel.ward = prevWard;
      }
    }
    initForm();

    wardService.findAllSummarized()
      .success(function (response) {
        $scope.wards = response;
        $scope.userHasNoAssociations = _.isEmpty($scope.wards);
        $scope.inputRecordModel.ward = $scope.wards[0];
      })
      .finally(function() {
        $scope.contentLoaded = true;
      });

    $scope.onSearchVoter = function () {
      function handleSuccess() {
        $scope.searchResults.push({});
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

        $('#electorId').focus();
      }
    };

    function mapFormToRequest(formModel) {
      var copy = $.extend(true, {}, formModel);
      copy.intention = _.parseInt(formModel.intention.charAt(0));
      copy.likelihood = _.parseInt(formModel.likelihood.charAt(0));
      copy.wardCode = formModel.ward.code;
      delete copy.ward;
      return copy;
    }

    function handleSubmitEntrySuccess(response) {
      $scope.elector = response;
      resetForm();

      $scope.logs.push({
        ern: response.ern,
        reason: '-',
        success: true
      });
    }

    function resetForm() {
      var prefix = util.extractErnPrefix($scope.inputRecordModel.ern);
      initForm();
      $scope.inputRecordModel.ern = prefix;
    }

    function handleSubmitEntryFailure(err) {
      resetForm();
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
    }

    function validateForm() {
      var errors = [];

      if (_.isEmpty($scope.inputRecordModel.ern)) {
        errors.push("Elector ID is empty");
      } else if (!util.validErn($scope.inputRecordModel.ern)) {
        errors.push("Elector ID is invalid");
      }

      return errors;
    }
  });
