angular
  .module('canvass')
  .directive('ukConstituencyMap', function (geoService) {
    return {
      restrict: 'E',
      templateUrl: 'views/partials/map.html',
      link: function (scope) {
        scope.legend = {};

        geoService
          .constituencyStatsTopoJsonMap('gb')
          .success(function (response) {
            var map = new google.maps.Map(document.getElementById('map'), {
              zoom: 7,
              zoomControl: true,
              mapTypeControl: false,
              streetViewControl: false,
              fullscreenControl: true,
              zoomControlOptions: {
                position: google.maps.ControlPosition.RIGHT_TOP
              },
              center: {lat: 52.5, lng: -2.0}
            });

            function styleMap() {
              map.set('styles', [
                {
                  featureType: 'all',
                  elementType: 'all',
                  stylers: [
                    { visibility: 'off' }
                  ]
                }
              ]);
            }

            styleMap();
            map.data.addGeoJson(response);
            var legend = document.getElementById('legend');
            console.log(legend);
            legend.index = 1;
            map.controls[google.maps.ControlPosition.RIGHT_TOP].push(legend);

            map.data.setStyle(function(feature) {
              return {
                fillColor: mapCountToFillColour(feature.H.count),
                strokeColor: 'gray',
                strokeWeight: 0.5,
                fillOpacity: 1
              };
            });

            map.data.addListener('mouseover', function(event) {
              scope.legend.constituency = event.feature.H.PCON13NM;
              scope.legend.canvassed = event.feature.H.count;
              if (!scope.$$phase) {
                scope.$apply();
              }

              map.data.revertStyle();
              map.data.overrideStyle(event.feature, {
                fillColor: '#0aa89e',
                fillOpacity: 0.8
              });
            });

            map.data.addListener('mouseout', function() {
              map.data.revertStyle();
            });

          });

        /**
         * Scales based on http://colorbrewer2.org/
         */
        function mapCountToFillColour(count) {
          if (count === 0) {
            return '#ffffff';
          } else if (count > 0 && count <= 50) {
            return '#fee5d9';
          } else if (count > 50 && count <= 100) {
            return '#fcbba1';
          } else if (count > 100 && count <= 250) {
            return '#fc9272';
          } else if (count > 250 && count <= 500) {
            return '#fb6a4a';
          } else if (count > 500 && count <= 750) {
            return '#ef3b2c';
          } else if (count > 750 && count <= 1000) {
            return '#cb181d';
          } else if (count > 1000) {
            return '#99000d';
          } else {
            return '#ffffff';
          }
        }
      }
    };
  });
