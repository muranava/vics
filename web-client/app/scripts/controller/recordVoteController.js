angular
  .module('canvass')
  .controller('recordVoteController', function ($timeout, $scope, RingBuffer, voteService, electorService, wardService) {
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
        $scope.formModel.selectedWard = $scope.wards[0];
      });

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function () {
      console.log($scope.formModel.selectedWard);
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

            $scope.formModel.ern = extractErnPrefix(response.ern);
          });
      }
    };

    /**
     * Allows the user to quickly type a sequence of records. In practice the first part (polling district)
     * does not change frequently
     */
    function extractErnPrefix(ern) {
      return _.head(ern.split('-')) + '-';
    }

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
      var parts = _.filter(electorID.split("-"), function (part) {
        return !_.isEmpty(part);
      });
      var valid = parts.length === 3 || parts.length === 2;
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
