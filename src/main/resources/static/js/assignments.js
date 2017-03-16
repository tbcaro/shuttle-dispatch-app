
function AssignmentApp(options) {
  var self = this;
  var elements = { };
  var geoLocator = { };

  self.map = { };
  self.mapMarkers = { };
  self.selectedDate = moment();
  self.assignments = { };

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.controlPanel = $('#control-panel');
    elements.assignmentCardContainer = $('#assignment-card-container');
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
          self.updateAssignments(response.data.assignmentDetailAdapters);
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

  self.updateAssignments = function(assignmentAdapters) {
    self.assignments = { };
    elements.assignmentCardContainer.empty();
    assignmentAdapters.forEach(function(assignmentData) {
      self.assignments[assignmentData.assignmentReport.assignmentId] = new Assignment(assignmentData);
      elements.assignmentCardContainer.append(self.assignments[assignmentData.assignmentReport.assignmentId].elements.card);
      self.assignments[assignmentData.assignmentReport.assignmentId].show();
    });
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

function Assignment(data) {
  var self = this;
  var templateId = '#assignment-details-card-template';

  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.card = $(templateId).find('.assignment-card').clone();
    self.elements.shuttleName = self.elements.card.find('.field-shuttle-name');
    self.elements.driverName = self.elements.card.find('.field-driver-name');
    self.elements.routeName = self.elements.card.find('.field-route-name');
    self.elements.startTime = self.elements.card.find('.field-start-time');
    self.elements.scheduleCard = self.elements.card.find('.schedule-card');
    self.elements.scheduleTableBody = self.elements.scheduleCard.find('tbody');

    self.update(data);
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.shuttleName = data.shuttleName;
    self.data.driverName = data.driverName;
    self.data.routeName = data.routeName;
    self.data.startTime = data.startTime;
    self.data.assignmentReport = data.assignmentReport;
  };

  self.bindData = function() {
    self.elements.card.data('assingmentId', self.data.assignmentReport.assignmentId);
    self.elements.shuttleName.html(self.data.shuttleName);
    self.elements.driverName.html(self.data.driverName);
    self.elements.routeName.html(self.data.routeName);
    self.elements.startTime.html(formatTime(self.data.startTime));

    // TBC : Bind assignment report to table
    if (self.data.assignmentReport != null) {
      self.bindAssignmentReportData();
    }
  };

  self.bindAssignmentReportData = function() {
    var report = self.data.assignmentReport;
    self.elements.scheduleTableBody.empty();

    for (var i = 0; i < report.assignmentStops.length; i++) {
      var row = $('<tr>');

      var order = $('<td>');
      var stopName = $('<td>');
      var address = $('<td>');
      var estArrive = $('<td>');
      var estWait = $('<td>');
      var estDepart = $('<td>');

      order.html(i + 1);
      stopName.html(report.assignmentStops[i].name);
      address.html(report.assignmentStops[i].address);
      estArrive.html(formatTime(report.assignmentStops[i].estArriveTime));
      estWait.html(formatWait(report.assignmentStops[i].estWaitTime));
      estDepart.html(formatTime(report.assignmentStops[i].estDepartTime));

      row.append(order);
      row.append(stopName);
      row.append(address);
      row.append(estArrive);
      row.append(estWait);
      row.append(estDepart);

      self.elements.scheduleTableBody.append(row);
    }
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
