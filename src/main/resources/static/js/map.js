
function MapApp(options) {
  var self = this;
  var elements = { };
  var geoLocator;
  var serviceCode = options.serviceCode;
  var intervalId;

  self.map = { };
  self.mapMarkers = { };
  self.shuttleActivities = [];

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.shuttleCardContainer = $('#active-shuttle-card-container');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();

    self.initializeMap();
    geoLocator.getLocation().done(function(location) {
      self.map.setCenter(location);
      self.map.setZoom(12);
      self.mapMarkers.userMarker = new google.maps.Marker({
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
    });

    self.loadShuttleActivities();
    intervalId = setInterval(self.loadShuttleActivities, 3000);
  };

  self.initializeMap = function() {
    self.map = new google.maps.Map(elements.mapContainer[0], {
      center: { lat: 39.8282, lng: -98.5795 }, // TBC : Default center over central USA
      zoom: 5
    });
  };

  self.loadShuttleActivities = function() {
    axios.get('/api/fetchAllShuttleActivity?serviceCode=' + serviceCode)
        .then(function (response) {
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
        });
  };

  self.initialize();
  return self;
}

function ShuttleActivity(serializedActivity) {
  var self = this;

  self.element = { };
  self.isSelected = false;

  self.constructor = function() {
    self.JSONString = serializedActivity.toString();
    self.shuttleName = serializedActivity.shuttleName;
    self.shuttleColorHex = serializedActivity.shuttleColorHex;
    self.shuttleLatitude = serializedActivity.shuttleLatitude;
    self.shuttleLongitude = serializedActivity.shuttleLongitude;
    self.shuttleHeading = serializedActivity.shuttleHeading;
    self.driverName = serializedActivity.driverName;
    self.shuttleStatus = serializedActivity.shuttleStatus;
    self.assignmentReport = serializedActivity.assignmentReport;

    self.generateElement();
  };

  self.bindEventHandlers = function() {
    self.element.on('click', function() {
      self.toggleSelected();
    });
  };

  self.toggleSelected = function() {
    if (self.isSelected)  {
      self.isSelected = false;
      self.element.removeClass('selected');
    } else {
      self.isSelected = true;
      self.element.addClass('selected');
    }
  };

  self.generateElement = function() {
    self.element = $('div');
    self.element.html(self.JSONString);
  };

  return self;
}