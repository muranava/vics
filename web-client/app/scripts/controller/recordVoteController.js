angular
  .module('canvass')
  .controller('recordVoteController', function (util, $timeout, $scope, RingBuffer, voteService, electorService, wardService, toastr) {
    var logSize = 7;

    $scope.searchResults = null;
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.formModel = {
      ern: {
        pollingDistrict: '',
        number: '',
        suffix: ''
      },
      selectedWard: null
    };

    $scope.searchForm = {
      surname: '',
      postcode: ''
    };

    $scope.wards = [];

    wardService.findAllSummarized()
      .success(function (response) {
        $scope.wards = response;
        $scope.userHasNoAssociations = _.isEmpty($scope.wards);
        $scope.formModel.selectedWard = $scope.wards[0];
      })
      .finally(function () {
        $scope.contentLoaded = true;
      });

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function () {
      var ern = $scope.formModel.ern.pollingDistrict + '-' +
        $scope.formModel.ern.number + '-' +
        $scope.formModel.ern.suffix;
      if (isValidElectorID(ern) &&
        isValidWard($scope.formModel.selectedWard)) {

        var elector = mapToRequest(ern);

        voteService.recordVote(elector)
          .success(function (response) {
            $scope.logs.push({
              ern: ern,
              reason: '',
              wardCode: response.wardCode,
              result: 1
            });
          })
          .error(function (error) {
            var reason = "-";
            if (error.type === 'PafApiNotFoundFailure') {
              reason = 'Voter not found';
            }
            $scope.logs.push({
              ern: ern,
              reason: reason,
              result: 0
            });
          });
        $scope.formModel.ern.suffix = 0;

        $('#electorNum').focus();
      }
    };

    function mapToRequest(ern) {
      return {
        ern: ern,
        wardCode: $scope.formModel.selectedWard.code,
        wardName: $scope.formModel.selectedWard.name
      };
    }

    function isValidWard(ward) {
      $scope.invalidWard = false;
      var isValid = ward && !_.isEmpty(ward.code);
      if (isValid) {
        return true;
      } else {
        $scope.invalidWard = true;
        return false;
      }
    }

    function isValidElectorID(electorID) {
      $scope.invalidErn = false;
      var valid = util.validErn($scope.formModel.selectedWard.code + "-" + electorID);
      if (valid) {
        return true;
      } else {
        $scope.invalidErn = true;
        return false;
      }
    }

    $scope.onUndo = function (model) {
      voteService.undoVote({
        wardCode: model.wardCode,
        ern: model.ern
      })
        .success(function () {
          model.reason = 'Undone';
        })
        .error(function () {
          toastr.error('Failed to undo vote', 'Error');
        });
    };

    $scope.onSearchVoter = function () {
      if (!$scope.searchForm.surname || !$scope.searchForm.postcode) {
        toastr.error('Please enter username and password', 'Validation Error');
      } else {
        electorService.search($scope.searchForm.surname, $scope.searchForm.postcode, $scope.formModel.selectedWard.code)
          .success(function (voters) {
            $scope.searchResults = voters;
          })
          .error(function handleError() {
            $scope.searchResults = [];
          });
      }
    };

    $scope.onSetSearchedVoter = function (voter) {
      var ern = util.destructureErn(voter.ern);

      $scope.formModel.ern = {
        pollingDistrict: ern.pollingDistrict,
        number: ern.number,
        suffix: ern.suffix
      };
    };

    function emptyRow() {
      return {
        wardCode: '-',
        pollingDistrict: '-',
        ern: '-',
        result: -1
      };
    }
  });
