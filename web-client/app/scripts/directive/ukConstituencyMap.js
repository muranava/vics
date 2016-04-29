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
              center: {lat: 52.5, lng: -2.0} // 54.5, -2.7
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
            map.controls[google.maps.ControlPosition.RIGHT_TOP].push(document.getElementById('legend'));

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


        function mapCountToFillColour(count) {
          if (count === 0) {
            return '#FFFFFF';
          } else if (count > 0 && count < 100) {
            return '#FBDCDA';
          } else if (count > 100 && count < 250) {
            return '#F5A9A3';
          } else if (count > 250 && count < 500) {
            return '#EF756C';
          } else {
            return '#E94135';
          }
        }
      }
    };
  });
