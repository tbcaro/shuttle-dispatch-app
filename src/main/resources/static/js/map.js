
function MapApp(options) {
  var self = this;
  var elements = { };
  var geoLocator;
  var serviceCode = options.serviceCode;
  var intervalId;

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
          self.updateShuttleMapMarkers();
        })
        .catch(function (error) {
          console.log(error);
          clearInterval(intervalId);
        });
  };

  self.bindEventHandlers = function() {
    elements.shuttleCardContainer.on('click', '.shuttle-card', function() {
      self.toggleCardSelected($(this));
      self.showSelectedRoute();
    });
  };

  self.toggleCardSelected = function(selectedCard) {
    var toSelectActivityId = selectedCard.data('activityId');
    var prevSelectedActivityId = elements.shuttleCardContainer.find('.selected').data('activityId');

    if (prevSelectedActivityId == null) {
      self.shuttleActivities[toSelectActivityId].select();
    } else if (toSelectActivityId === prevSelectedActivityId){
      self.shuttleActivities[toSelectActivityId].deselect();
    } else {
      self.shuttleActivities[toSelectActivityId].select();
      self.shuttleActivities[prevSelectedActivityId].deselect();
    }
  };

  self.showSelectedRoute = function() {

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

  self.updateShuttleMapMarkers = function() {

  };

  self.initialize();
  return self;
}

function ShuttleActivity(data) {
  var self = this;
  var templateId = '#shuttle-activity-card-template';

  self.container = { };
  self.elements = { };
  self.data = { };
  self.isSelected = false;

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
    }
  };

  self.bindAssignmentReportData = function() {
    var report = self.data.assignmentReport;
    self.elements.scheduleTableBody.empty();

    for (var i = 0; i < report.stops.length; i++) {
      var row = $('<tr>');

      var icon = $('<td>');
      var order = $('<td>');
      var stopName = $('<td>');
      var address = $('<td>');
      var estArrive = $('<td>');
      var estWait = $('<td>');
      var actDepart = $('<td>');

      icon.append($('<i>').addClass('fa'));
      if (report.currentStop < i) {
        icon.find('i').addClass('fa-square-o');
      } else if (report.currentStop === i) {
        icon.find('i').addClass('fa-spinner');
      } else {
        icon.find('i').addClass('fa-check-square-o');
      }

      order.html(i + 1);
      stopName.html(report.stops[i].name);
      address.html(report.stops[i].address);
      estArrive.html(formatTime(report.stops[i].estArriveTime));
      estWait.html(formatWait(report.stops[i].estWaitTime));
      actDepart.html(formatTime(report.stops[i].actualDepartTime));

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

  self.select = function() {
    self.isSelected = true;
    self.elements.card.addClass('selected');
  };

  self.deselect = function() {
    self.isSelected = false;
    self.elements.card.removeClass('selected');
  };

  self.show = function() {
    self.elements.card.show();
  };

  self.hide = function() {
    self.elements.card.hide();
  };

  var formatTime = function(localTime) {
    var min = 0;
    var hr = 0;
    var ampm = '';
    var strBuilder = '';

    if (localTime == null) {
      strBuilder = '--';
    } else {
      if (localTime.hour - 12 > 0) {
        hr = localTime.hour - 12;
        ampm = 'PM';
      } else {
        hr = localTime.hour;
        ampm = 'AM';
      }
      strBuilder += hr.toString() + ":";

      min = localTime.minute;
      if (min < 10) {
        strBuilder += '0' + min.toString();
      } else {
        strBuilder += min.toString();
      }

      strBuilder += ' ' + ampm.toString();
    }

    return strBuilder;
  };

  var formatWait = function(waitMins) {
    var mins = 0;
    var hrs = 0;
    var strBuilder = '';

    if (waitMins == null) {
      strBuilder = '--';
    } else if (waitMins / 60 < 1) {
      strBuilder += waitMins.toString() + 'mins';
    } else {
      hrs = Math.floor(waitMins / 60);
      mins = waitMins - (60 * hrs);

      strBuilder += hrs.toString();
      hrs === 1 ? strBuilder += 'hr' : strBuilder += 'hrs';
      if(mins > 0) strBuilder += mins.toString() + 'mins';
    }

    return strBuilder;
  };

  self.initialize();
  return self;
}