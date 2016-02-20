angular
  .module('canvass')
  .controller('recordVoteController', function ($scope, RingBuffer, voteService) {
    var logSize = 7;

    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);
    $scope.rollNum = "";
    $scope.pollingDistrict = "";

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function() {
      voteService.recordVote(formatErn($scope.rollNum, $scope.pollingDistrict))
        .success(function(response) {
          $scope.logs.push({
            pd: response.pollingDistrict,
            rollNum: response.rollNum,
            firstName: response.firstName,
            lastName: response.lastName,
            result: response.success ? 1 : 0
          });
        });

      $scope.rollNum = "";
    };

    function formatErn(pollingDistrict, rollNum) {
      return pollingDistrict + rollNum;
    }

    $scope.onSearchVoted = function() {
      $scope.onVote();
    };

    $scope.onSearch = function() {
      throw Error("Not yet implemented");
    };

    function emptyRow() {
      return {
        pd: '-',
        rollNum: '-',
        firstName: '-',
        lastName: '-',
        result: -1
      };
    }
  });
