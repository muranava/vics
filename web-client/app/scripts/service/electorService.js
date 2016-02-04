angular
    .module('canvass')
    .service('electorService', function (apiUrl, $http) {
        var api = {};

        /**
         * Retrieves a preview of the voters by the given constituency
         * and wards
         * @param {String} constituencyName
         * @param {String[]} wardNames
         * @return {Object}
         */
        api.previewVotersByWards = function (constituencyName, wardNames) {
            return $http.post(apiUrl + '/elector/preview', {
                constituencyName: constituencyName,
                wardNames: wardNames
            });
        };

        /**
         * Retrieves the elector records for the given
         * @param {String[]} wardCodes
         * @return {Object[]}
         */
        api.retrieveElectorsByWards = function(wardCodes) {
            return $http.post(apiUrl + '/elector/print', {
                wardCodes: wardCodes
            });
        };

        /**
         * Retrieves the elector records for the given
         * @param {String[]} wardCodes
         * @return {Object[]}
         */
        api.retrieveEnrichedElectorsByWards = function(wardCodes) {
            return $http.post(apiUrl + '/elector/print/enriched', {
                wardCodes: wardCodes
            });
        };

        return api;
    });
