function App(options) {
  var self = this;

  var elements = { };

  self.geoLocator = { };
  self.googleMapsLoader = { };
  self.map = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.activeShuttleCardContainer = $('#active-shuttle-card-container');

    // TBC : Setup utility objects
    self.geoLocator = new GeoLocator();
    
    self.geoLocator.getLocation().done(function (location) {
      self.initializeMap(10, location);
    });
  };

  self.initializeMap = function(_zoom, _center) {
    var zoom = _zoom || 10;
    var center = _center || { lat: 100, lng: 100 };

    self.map = new google.maps.Map(elements.mapContainer[0], {
      center: center,
      zoom: zoom
    });
  };

  var bindEventHandlers = function() {

  };
}
