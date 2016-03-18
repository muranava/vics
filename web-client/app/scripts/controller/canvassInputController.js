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
        ern: {
          pollingDistrict: '',
          number: '',
          suffix: ''
        },
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
        ward: null,
        telephone: null,
        email: null
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
      .finally(function () {
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

    $scope.onUndo = function(model) {
      electorService.undoCanvassInput(model.fullErn, model.contactId)
        .success(function() {
          model.reason = 'Undone';
        })
        .error(function() {
          model.reason = 'Failed to undo';
          model.success = false;
        });
    };

    /**
     * Submits the entry form and adds the result to the log.
     */
    $scope.onSubmitRecord = function () {
      $scope.errors = validateForm();

      if (_.isEmpty($scope.errors)) {
        var requestData = mapFormToRequest($scope.inputRecordModel);
        var ern = constructErnFromParts($scope.inputRecordModel.ern, $scope.inputRecordModel.ward.code);
        electorService.submitCanvassInput(ern, requestData)
          .success(function(response) {
            $scope.elector = response;
            $scope.logs.push({
              ern: stripWardCodeFromErn(response.ern),
              reason: '-',
              fullErn: ern,
              contactId: response.id,
              success: true
            });
            resetForm();
          })
          .error(function (err) {
            resetForm();
            if (err && err.type === 'PafApiNotFoundFailure') {
              $scope.logs.push({
                ern: stripWardCodeFromErn(ern),
                fullErn: ern,
                reason: 'Voter not found',
                success: false
              });
            } else {
              $scope.logs.push({
                ern: stripWardCodeFromErn(ern),
                fullErn: ern,
                reason: 'Failed to record contact',
                success: false
              });
            }
          });
      }
      scrollToInput();
    };

    function scrollToInput() {
      $("html, body").animate({scrollTop: $('#canvassEntry').offset().top}, 500);
      $('#electorNum').focus();
    }

    function mapFormToRequest(formModel) {
      var copy = $.extend(true, {}, formModel);
      copy.intention = _.parseInt(formModel.intention.charAt(0));
      copy.likelihood = _.parseInt(formModel.likelihood.charAt(0));
      delete copy.ward;

      return copy;
    }

    function stripWardCodeFromErn(fullErn) {
      var parts = fullErn.split("-");
      parts.splice(0, 1);
      return parts.join("-");
    }

    function resetForm() {
      var prefix = $scope.inputRecordModel.ern.pollingDistrict,
        suffix = $scope.inputRecordModel.ern.suffix;
      initForm();
      $scope.inputRecordModel.ern.pollingDistrict = prefix;
      $scope.inputRecordModel.ern.suffix = suffix;
    }

    function constructErnFromParts(model, wardCode) {
      return wardCode + '-' + model.pollingDistrict + '-' +
        model.number + '-' +
        model.suffix;
    }

    function validateForm() {
      var errors = [];

      var ern = constructErnFromParts($scope.inputRecordModel.ern, $scope.inputRecordModel.ward.code);

      if (!util.validErn(ern)) {
        errors.push("Elector ID is invalid");
      }

      return errors;
    }
  });
