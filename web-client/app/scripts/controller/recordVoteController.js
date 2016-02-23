angular
  .module('canvass')
  .controller('recordVoteController', function ($scope, RingBuffer, voteService) {
    var logSize = 7;

    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);

    $scope.formModel = {
      wardCode: '',
      pollingDistrict: '',
      ern: ''
    };

    _.times(logSize, function () {
      $scope.logs.push(emptyRow());
    });

    $scope.onVote = function() {
      voteService.recordVote($scope.formModel)
        .success(function(response) {
          $scope.logs.push({
            pollingDistrict: response.pollingDistrict,
            ern: response.ern,
            wardCode: response.wardCode,
            result: response.success ? 1 : 0
          });

          $scope.formModel.ern = "";
        });
    };

    $scope.onSearchVoted = function() {
      $scope.onVote();
    };

    $scope.onSearch = function() {
      throw Error("Not yet implemented");
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
