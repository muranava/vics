/**
 * Service to query the health of external systems
 */
angular
    .module('canvass')
    .service('healthService', function(apiUrl, $http) {
        var api = {};

        /**
         * Gets the health of the api
         * @returns {Promise<Object>} the health status
         */
        api.retrieveOwnHealthStatus = function() {
            return $http.get(apiUrl + '/health');
        };

        return api;
    });