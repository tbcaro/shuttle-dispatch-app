
function MapApp(options) {
  var self = this;
  var elements = { };
  var geoLocator;
  var serviceCode = options.serviceCode;
  var intervalId;
  var shuttleActivityCardtemplate;

  self.map = { };
  self.mapMarkers = { };
  self.shuttleActivities = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.shuttleCardContainer = $('#active-shuttle-card-container');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();

    // TBC : Compile templates
    shuttleActivityCardtemplate = Handlebars.compile($("#shuttle-activity-card-template").html());

    // TBC : Initialize map
    self.initializeMap();

    // TBC : Center map on user location
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

      // TBC : Bind event handlers
      self.bindEventHandlers();
    });

    // TBC : Begin loading shuttle activities from server
    self.loadShuttleActivities();
    intervalId = setInterval(self.loadShuttleActivities, 5000);
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
          self.updateShuttleCards(response.data);
        })
        .catch(function (error) {
          console.log(error);
          clearInterval(intervalId);
        });
  };

  self.bindEventHandlers = function() {
    elements.shuttleCardContainer.on('click', '.shuttle-card', function() {
      var activityId = $(this).data('activity-id');
      var activity = self.shuttleActivities[activityId];
      self.clearSelectedCards();
      // activity.setSelected(true);
    });
  };

  self.clearSelectedCards = function() {
    elements.shuttleCardContainer.find('.selected').removeClass('selected');
  };

  self.updateShuttleCards = function(data) {
    data.forEach(function(activityData) {
      var activity;
      // TBC : If activity has already been loaded, update it.
      if (self.shuttleActivities.hasOwnProperty(activityData.activityId.toString())) {
        activity = self.shuttleActivities[activityData.activityId];
        activity.update(activityData);
      } else { // TBC : If activity is not present, create it
        activity = new ShuttleActivity(activityData);
        activity.appendTo(elements.shuttleCardContainer);
        activity.show();
        self.shuttleActivities[activity.data.activityId] = activity;
      }
    });
  };

  self.initialize();
  return self;
}

// function ShuttleActivity(serializedActivity, template) {
//   var self = this;
//
//   self.element;
//   self.data = { };
//   self.isSelected = false;
//
//   self.initialize = function() {
//     self.updateData(serializedActivity);
//
//     // TBC : Generate element from template
//     self.element = self.generateElement();
//   };
//
//   self.updateData = function(activity) {
//     for (var key in activity) {
//       self.data[key] = activity[key];
//     }
//   };
//
//   self.generateElement = function() {
//     return $(template(self.data)).filter('.shuttle-card');
//   };
//
//   self.setSelected = function(selected) {
//     self.isSelected = selected;
//     if (selected) {
//       self.element.addClass('selected');
//     }
//
//   };
//
//   self.initialize();
//   return self;
// }

function ShuttleActivity(data) {
  var self = this;
  var templateId = '#shuttle-activity-card-template';

  self.container = { };
  self.elements = { };
  self.data = { };
  self.isSelected = false;

  self.initialize = function() {
    self.elements.card = $(templateId).clone();
    self.elements.shuttleIcon = self.elements.card.find('.shuttle-icon');
    self.elements.shuttleName = self.elements.card.find('.field-shuttle-name');
    self.elements.driverName = self.elements.card.find('.field-driver-name');
    self.elements.currentStopName = self.elements.card.find('.field-stop-name');
    self.elements.statusLabel = self.elements.card.find('.state-label');

    self.update(data);
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.activityId = data.activityId;
    self.data.shuttleName = data.shuttleName;
    self.data.shuttleColorHex = data.shuttleColorHex;
    self.data.shuttleLatitude = data.shuttleLatitude;
    self.data.shuttleLongitude = data.shuttleLongitude;
    self.data.shuttleHeading = data.shuttleHeading;
    self.data.driverName = data.driverName;
    self.data.shuttleStatus = data.shuttleStatus;
    self.data.assignmentReport = data.assignmentReport;
    self.data.currentStopName = data.currentStopName;
  };

  self.bindData = function() {
    self.elements.card.data('activity-id', self.data.activityId);
    self.elements.shuttleIcon.css('color', self.data.shuttleColorHex);
    self.elements.shuttleName.html(self.data.shuttleName);
    self.elements.driverName.html(self.data.driverName);
    self.elements.currentStopName.html(self.data.currentStopName);

    // TBC : Reset status label
    self.elements.statusLabel.removeClass('btn-info');
    self.elements.statusLabel.removeClass('btn-success');
    self.elements.statusLabel.removeClass('btn-warning');
    switch (self.data.shuttleStatus) {
      case 'ACTIVE':
        self.elements.statusLabel.addClass('btn-info');
        self.elements.statusLabel.html('Active');
        break;
      case 'DRIVING':
        self.elements.statusLabel.addClass('btn-success');
        self.elements.statusLabel.html('Driving');
        break;
      case 'AT_STOP':
        self.elements.statusLabel.addClass('btn-warning');
        self.elements.statusLabel.html('At-Stop');
        break;
    }
  };

  self.appendTo = function(container) {
    container.append(self.elements.card);
  };

  self.select = function() {
    self.isSelected = true;
    self.addClass('selected');
  };

  self.show = function() {
    self.elements.card.show();
  };

  self.hide = function() {
    self.elements.card.hide();
  };

  self.initialize();
  return self;
}