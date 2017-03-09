function App(options) {
  var self = this;

  var elements = { };

  var geoLocator = { };

  self.map = { };
  self.mapMarkers = [];

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.activeShuttleCardContainer = $('#active-shuttle-card-container');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();
    
    geoLocator.getLocation().done(function (location) {
      self.initializeMap(location);
    });
  };

  self.initializeMap = function(location) {
    var zoom = 15;
    var center = location || { lat: 41.208151, lng: -79.378834 }; // TBC : Default over Clarion University

    self.map = new google.maps.Map(elements.mapContainer[0], {
      center: center,
      zoom: zoom
    });

    var marker = new google.maps.Marker({
      position: location,
      map: self.map,
      icon: {
        path: fontawesome.markers.MAP_MARKER,
        scale: 0.5,
        strokeWeight: 0.2,
        strokeColor: 'black',
        strokeOpacity: 1,
        fillColor: '#ff0000',
        fillOpacity: 1,
        rotation: 0
      }
    });

    self.mapMarkers.push(marker);
  };

  var bindEventHandlers = function() {

  };

  return self;
}
