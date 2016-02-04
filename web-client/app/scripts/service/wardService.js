/**
 * Service to retrieve wards and constituencies
 */
angular
    .module('canvass')
    .service('wardService', function ($http, apiUrl) {
        var api = {};

        /**
         * Finds the wards within constituency
         * @param {String} name the constituency name
         */
        api.findWardsWithinConstituency = function (name) {
            return $http.get(apiUrl + '/ward', {
                params: {
                    constituency: name
                },
                cache: true
            });
        };

        return api;
    });
