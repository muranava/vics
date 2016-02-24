angular
  .module('canvass')
  .controller('recordVoteController', function (util, $timeout, $scope, RingBuffer, voteService, electorService, wardService) {
    var logSize = 7,
      searchLimit = 10;

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
      });

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function () {
      if (isValidElectorID($scope.formModel.ern) &&
          isValidWard($scope.formModel.selectedWard)) {

        var elector = mapToRequest($scope.formModel);

        voteService.recordVote(elector)
          .success(function (response) {
            $scope.logs.push({
              pollingDistrict: response.pollingDistrict,
              ern: response.ern,
              wardName: response.wardName,
              result: response.success ? 1 : 0
            });

            $scope.formModel.ern = util.extractErnPrefix(response.ern);
          });
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
      $scope.onVote();
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
