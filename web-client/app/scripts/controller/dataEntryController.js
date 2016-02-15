angular
  .module('canvass')
  .controller('dataEntryController', function ($scope, RingBuffer) {
    var logSize = 7;

    // FIXME remove stub data for demo

    $scope.searchResults = [];
    $scope.logs = RingBuffer.newInstance(logSize);
    $scope.rollNum = "";
    $scope.pollingDistrict = "";

    _.times(logSize, function () {
      $scope.logs.push(emptyRow())
    });

    $scope.onVote = function() {
      $scope.logs.push({
        pd: $scope.pollingDistrict,
        rollNum: $scope.rollNum,
        firstName: 'Jon',
        lastName: 'Baines',
        result: _.random(0, 1)
      });

      $scope.rollNum = "";
    };

    $scope.onSearchVoted = function() {
      $scope.onVote();
    };

    $scope.onSearch = function() {
      $scope.searchResults.push({
        rollNum: 'AAB/13451/2',
        firstName: 'Amy',
        lastName: 'Leigh',
        address: '31 Mole Avenue',
        postCode: 'AB2 9AP'
      })
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
