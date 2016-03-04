angular
  .module('canvass')
  .controller('recordVoteController', function (util, $timeout, $scope, RingBuffer, voteService, electorService, wardService) {
    var logSize = 7,
      searchLimit = 10,
      electorIdElement = $('#electorId');

    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.formModel = {
      ern: '',
      selectedWard: null
    };

    $scope.searchModel = {
      firstName: '',
      lastName: '',
      postCode: '',
      address: ''
    };

    $scope.wards = [];

    wardService.findAllSummarized()
      .success(function(response) {
        $scope.wards = response;
        $scope.userHasNoAssociations = _.isEmpty($scope.wards);
        $scope.formModel.selectedWard = $scope.wards[0];
      })
      .finally(function() {
        $scope.contentLoaded = true;
      });

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function () {
      var ern = $scope.formModel.ern;
      if (isValidElectorID(ern) &&
          isValidWard($scope.formModel.selectedWard)) {

        var elector = mapToRequest($scope.formModel);

        voteService.recordVote(elector)
          .success(function () {
            $scope.logs.push({
              ern: ern,
              reason: '',
              result: 1
            });
          })
          .error(function(error) {
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
        $scope.formModel.ern = util.extractErnPrefix(ern);

        electorIdElement.focus();
      }
    };

    function mapToRequest(model) {
      // removes any trailing hyphens
      var sanitizedErn = _.filter(model.ern.split('-'), function (part) {
        return !_.isEmpty(part);
      }).join('-');

      return {
        ern: sanitizedErn,
        wardCode: model.selectedWard.code,
        wardName: model.selectedWard.name
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
      var valid = util.validErn(electorID);
      if (valid) {
        return true;
      } else {
        $scope.invalidErn = true;
        return false;
      }
    }

    $scope.onSearchVoted = function () {

    };

    $scope.onSearch = function () {
      electorService.search($scope.searchModel, searchLimit)
        .success(function (response) {
          $scope.searchResults = response;
        });
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
