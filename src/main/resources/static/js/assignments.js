
function AssignmentApp(options) {
  var self = this;
  var elements = { };
  var geoLocator = { };

  self.map = { };
  self.mapMarkers = { };
  self.selectedDate = moment();

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.controlPanel = $('#control-panel');
    elements.btnPrevDay = elements.controlPanel.find('#btn-prevday');
    elements.btnSelectedDate = elements.controlPanel.find('#btn-selecteddate');
    elements.btnNextDay = elements.controlPanel.find('#btn-nextday');
    elements.txtBoxAddress = elements.controlPanel.find('#txtbox-address');
    elements.btnSearchAddress = elements.controlPanel.find('#btn-search');
    elements.btnNewAssignment = elements.controlPanel.find('#btn-new-assignment');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();
    self.initializeMap();
    geoLocator.getLocation().done(function(location) {
      self.map.setCenter(location);
      self.map.setZoom(12);
      self.mapMarkers.stopMarker = new google.maps.Marker({
            position: location,
            map: self.map,
            icon: {
              path: fontawesome.markers.MAP_PIN,
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

    self.setSelectedDate(options.selectedDate);
    self.fetchAssignments(self.selectedDate);
    bindEventHandlers();
  };

  self.initializeMap = function() {
    self.map = new google.maps.Map(elements.mapContainer[0], {
      center: { lat: 39.8282, lng: -98.5795 }, // TBC : Default center over central USA
      zoom: 5,
      disableDefaultUI: true
    });
  };

  self.fetchAssignments = function(date) {
    axios.get('/test/api/fetchAllAssignments?date=' + date.toISOString())
        .then(function(response){
          console.log(response);
          self.setSelectedDate(response.data.selectedDate);
          elements.btnSelectedDate.html(response.data.displayDate);
        })
        .catch(function(error){
          console.log(error);
        });
  };

  self.setSelectedDate = function(localDate) {
    self.selectedDate = moment([
          localDate.year,
          localDate.monthValue - 1,
          localDate.dayOfMonth
        ]);
  };

  var bindEventHandlers = function() {
    elements.btnPrevDay.on('click', function() {
      var day = moment(self.selectedDate);
      day.subtract(1, 'd');
      self.fetchAssignments(day);
    });

    elements.btnNextDay.on('click', function() {
      var day = moment(self.selectedDate);
      day.add(1, 'd');
      self.fetchAssignments(day);
    });
  };

  self.initialize();
  return self;
}
