angular
  .module('canvass')
  .controller('recordVoteController', function (util, $timeout, $scope, RingBuffer, voteService, electorService, wardService) {
    var logSize = 7,
      searchLimit = 10,
      electorIdElement = $('#electorNum');

    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.formModel = {
      ern: {
        pollingDistrict: '',
        number: '',
        suffix: ''
      },
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
      var ern = $scope.formModel.ern.pollingDistrict + '-' +
        $scope.formModel.ern.number + '-' +
        $scope.formModel.ern.suffix;
      if (isValidElectorID(ern) &&
          isValidWard($scope.formModel.selectedWard)) {

        var elector = mapToRequest(ern);

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
        $scope.formModel.ern.number = '';

        electorIdElement.focus();
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
