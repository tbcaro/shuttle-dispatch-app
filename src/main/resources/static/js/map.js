
function MapApp(options) {
  var self = this;
  var elements = { };
  var geoLocator = { };
  var intervalId = 0;
  var initialLoad = true;
  var selectedActivityCardId = null;
  var selectedAssignment = null;

  self.map = { };
  self.mapMarkers = { };
  self.shuttleActivities = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.shuttleCardContainer = $('#active-shuttle-card-container');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();

    // TBC : Initialize map
    self.initializeMap();

    // TBC : Center map on user location
    geoLocator.getLocation().done(function(location) {
      self.map.setCenter(location);
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

      self.fitMapToBounds();
    });

    // TBC : Bind event handlers
    self.bindEventHandlers();

    self.mapMarkers.selectedRouteMarkers = [];

    // TBC : Begin loading shuttle activities from server
    self.loadShuttleActivities();
    intervalId = setInterval(self.loadShuttleActivities, 3000);
  };

  self.initializeMap = function() {
    self.map = new google.maps.Map(elements.mapContainer[0], {
      center: { lat: 39.8282, lng: -98.5795 }, // TBC : Default center over central USA
      zoom: 5,
      disableDefaultUI: true
    });
  };

  self.fitMapToBounds = function() {
    var bounds = new google.maps.LatLngBounds();

    // TBC : Extend bounds to shuttle positions
    if (self.mapMarkers.hasOwnProperty('busMarkers') && self.mapMarkers.busMarkers.length > 0) {
      // self.mapMarkers.busMarkers.forEach(function(marker) {
      //   bounds.extend(marker.getPosition());
      // });
      //
      // self.map.fitBounds(bounds);
    } else {

      // TBC : Extend bounds to user position
      if (self.mapMarkers.hasOwnProperty('userMarker')) {
        var userPosition = self.mapMarkers.userMarker.getPosition();
        bounds.extend({
                        lat: userPosition.lat() - .0005,
                        lng: userPosition.lng() - .0005
                      });
        bounds.extend({
                        lat: userPosition.lat() + .0005,
                        lng: userPosition.lng() + .0005
                      });

        self.map.fitBounds(bounds);
      }
    }
  };

  self.loadShuttleActivities = function() {
    axios.get('/api/fetchAllShuttleActivity')
        .then(function (response) {
          self.updateShuttleCards(response.data);
          self.updateShuttleMapMarkers();

          self.showSelectedRoute();
          if (selectedActivityCardId != null) {
            selectedAssignment = self.shuttleActivities[selectedActivityCardId].data.assignmentReport;
          }

          if(initialLoad) {
            self.fitMapToBounds();
          }
          initialLoad = false;
        })
        .catch(function (error) {
          console.log(error);
          selectedActivityCardId = null;
          selectedAssignment = null;
          self.showSelectedRoute();
        });
  };

  self.bindEventHandlers = function() {
    elements.shuttleCardContainer.on('click', '.shuttle-card', function() {
      self.toggleCardSelected($(this));
      self.showSelectedRoute(true);
    });
  };

  self.toggleCardSelected = function(selectedCard) {
    if (selectedCard.hasClass('selected')) {
      selectedActivityCardId = null;
      selectedAssignment = null;
      selectedCard.removeClass('selected');
    } else {
      selectedActivityCardId = selectedCard.data('activityId');
      selectedAssignment = self.shuttleActivities[selectedActivityCardId].data.assignmentReport;
      elements.shuttleCardContainer.find('.selected').removeClass('selected');
      selectedCard.addClass('selected');
    }
  };

  self.showSelectedRoute = function(initialOverride) {
    if (selectedActivityCardId == null) {
      if (self.mapMarkers.hasOwnProperty('selectedRouteMarkers')
          && self.mapMarkers.selectedRouteMarkers.length > 0) {
        // Remove all markers from map
        self.mapMarkers.selectedRouteMarkers.forEach(function (marker) {
          marker.setMap(null);
        });
      }
      self.mapMarkers.selectedRouteMarkers = [];
    } else if (initialOverride || self.selectedAssignmentHasChanged(self.shuttleActivities[selectedActivityCardId].data.assignmentReport)) {
      var activity = self.shuttleActivities[selectedActivityCardId];

      if (self.mapMarkers.hasOwnProperty('selectedRouteMarkers')
          && self.mapMarkers.selectedRouteMarkers.length > 0) {
        // Remove all markers from map
        self.mapMarkers.selectedRouteMarkers.forEach(function (marker) {
          marker.setMap(null);
        });
      }
      self.mapMarkers.selectedRouteMarkers = [];

      if (activity.data.assignmentReport != null) {
        activity.data.assignmentReport.assignmentStops.forEach(function (assignmentStop) {
          var markerColor = '';

          if (activity.data.assignmentReport.currentStop > assignmentStop.order) {
            markerColor = 'grey';
          } else if (activity.data.assignmentReport.currentStop === assignmentStop.order) {
            markerColor = 'yellow';
          } else {
            markerColor = activity.data.shuttleColorHex;
          }

          self.mapMarkers.selectedRouteMarkers.push(new google.maps.Marker({
            position: {
              lat: assignmentStop.lat,
              lng: assignmentStop.long
            },
            map: self.map,
            icon: {
              path: fontawesome.markers.MAP_PIN,
              scale: 0.5,
              strokeWeight: 0.2,
              strokeColor: 'black',
              strokeOpacity: 1,
              fillColor: markerColor,
              fillOpacity: 1,
              rotation: 0
            }
          }));
        });
      }
    }
  };

  self.selectedAssignmentHasChanged = function(newAssignment) {
    if (selectedAssignment == null && newAssignment != null) return true;
    if (selectedAssignment != null && newAssignment == null) return true;
    if (selectedAssignment != null && newAssignment != null) {
      if (selectedAssignment.currentStop != newAssignment.currentStop) return true;
      if (selectedAssignment.assignmentStops.length != newAssignment.assignmentStops.length) return true;
    }

    return false;
  };

  self.updateShuttleCards = function(data) {
    for(var activityId in self.shuttleActivities) {
      var found = false;

      data.forEach(function(activityData) {
        if (activityData.activityId == activityId)
          found = true;
      });

      if (!found) {
        var activity = self.shuttleActivities[activityId];

        activity.elements.card.remove();
        delete self.shuttleActivities[activityId];
      }
    }
    if (data.length == 0) {
      var noResultsCard = elements.shuttleCardContainer.find('.no-results-card');
      if(!noResultsCard.length) {
        var card = $('<div>').addClass('card').addClass('m-2').addClass('p-5').addClass('no-results-card').addClass('text-center');
        card.html('No shuttles currently active');
        elements.shuttleCardContainer.append(card);
      }
    } else {
      var noResultsCard = elements.shuttleCardContainer.find('.no-results-card');
      if(noResultsCard.length) {
        noResultsCard.remove();
      }
      data.forEach(function (activityData) {
        var activity;
        // TBC : If activity has already been loaded, update it.
        if (self.shuttleActivities.hasOwnProperty(activityData.activityId.toString())) {
          activity = self.shuttleActivities[activityData.activityId];
          activity.update(activityData);
        } else { // TBC : If activity is not present, create it
          activity = new ShuttleActivity(activityData);
          self.shuttleActivities[activity.data.activityId] = activity;
          activity.appendTo(elements.shuttleCardContainer);
          activity.show();
        }
      });
    }
  };

  self.updateShuttleMapMarkers = function() {
    if (self.mapMarkers.busMarkers != null) {
      self.mapMarkers.busMarkers.forEach(function(marker){
        marker.setMap(null);
      });
    }
    self.mapMarkers.busMarkers = [];

    for (var activityId in self.shuttleActivities) {
      var activity = self.shuttleActivities[activityId];

      self.mapMarkers.busMarkers.push(new google.maps.Marker({
        position: {
          lat: activity.data.shuttleLatitude,
          lng: activity.data.shuttleLongitude
        },
        map: self.map,
        icon: {
          path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
          scale: 5,
          strokeColor: 'lightgrey',
          strokeOpacity: 0.5,
          strokeWeight: 0.2,
          rotation: activity.data.shuttleHeading,
          fillColor: activity.data.shuttleColorHex,
          fillOpacity: 1
        }
      }));
    }
  };

  self.initialize();
  return self;
}

function ShuttleActivity(data) {
  var self = this;
  var templateId = '#shuttle-activity-card-template';
  var timeUtils = new TimeUtils();

  self.container = { };
  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.card = $(templateId).find('.shuttle-card').clone();
    self.elements.shuttleIcon = self.elements.card.find('.shuttle-icon');
    self.elements.shuttleName = self.elements.card.find('.field-shuttle-name');
    self.elements.driverName = self.elements.card.find('.field-driver-name');
    self.elements.currentStopName = self.elements.card.find('.field-stop-name');
    self.elements.statusLabel = self.elements.card.find('.state-label');
    self.elements.scheduleCard = self.elements.card.find('.schedule-card');
    self.elements.scheduleTableBody = self.elements.scheduleCard.find('tbody');

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
    self.elements.card.data('activityId', self.data.activityId);
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

    // TBC : Bind assignment report to table
    if (self.data.assignmentReport != null) {
      self.bindAssignmentReportData();
    } else {
      self.elements.scheduleTableBody.empty();
    }
  };

  self.bindAssignmentReportData = function() {
    var report = self.data.assignmentReport;
    self.elements.scheduleTableBody.empty();

    for (var i = 0; i < report.assignmentStops.length; i++) {
      var row = $('<tr>');

      var icon = $('<td>');
      var order = $('<td>');
      var stopName = $('<td>');
      var address = $('<td>');
      var estArrive = $('<td>');
      var estWait = $('<td>');
      var actDepart = $('<td>');

      icon.append($('<i>').addClass('fa'));
      if (report.currentStop === i) {
        icon.find('i').addClass('fa-spinner');
      } else if (report.currentStop > i) {
        icon.find('i').addClass('fa-check');
      }

      order.html(i + 1);
      stopName.html(report.assignmentStops[i].name);
      address.html(report.assignmentStops[i].address);
      estArrive.html(timeUtils.formatTime(report.assignmentStops[i].estArriveTime));
      estWait.html(timeUtils.formatWait(report.assignmentStops[i].estWaitTime));
      actDepart.html(timeUtils.formatTime(report.assignmentStops[i].actualDepartTime));

      row.append(icon);
      row.append(order);
      row.append(stopName);
      row.append(address);
      row.append(estArrive);
      row.append(estWait);
      row.append(actDepart);

      self.elements.scheduleTableBody.append(row);
    }
  };

  self.appendTo = function(container) {
    container.append(self.elements.card);
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