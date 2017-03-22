
function AssignmentApp(options) {
  var self = this;
  var elements = { };
  var geoLocator = { };
  var formTypes = {
    CREATE: 'create',
    EDIT: 'edit'
  };

  self.map = { };
  self.mapMarkers = { };
  self.selectedDate = moment();
  self.assignmentCards = { };
  self.assignmentForm = null;

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
    self.assignmentCards = { };
    elements.assignmentCardContainer.empty();
    assignmentAdapters.forEach(function(assignmentData) {
      self.assignmentCards[assignmentData.assignmentReport.assignmentId] = new Assignment(assignmentData);
      elements.assignmentCardContainer.append(self.assignmentCards[assignmentData.assignmentReport.assignmentId].elements.card);
      self.assignmentCards[assignmentData.assignmentReport.assignmentId].show();
    });
  };

  self.loadNewAssignmentForm = function() {
    axios.get('/test/api/assignment/formOptions')
        .then(function(response){
          console.log(response);
          self.addAssignmentForm(response.data);
        })
        .catch(function(error){
          console.log(error);
        });
  };

  self.addAssignmentForm = function(options) {
    self.removeAssignmentForm();

    self.assignmentForm = new AssignmentForm(options);
    elements.assignmentCardContainer.prepend(self.assignmentForm.elements.form);
    self.assignmentForm.elements.shuttleSelector[0].focus(); // TBC : Set focus to the element
  };

  self.removeAssignmentForm = function() {
    if (self.assignmentForm != null) {
      self.assignmentForm.elements.form.remove();
      self.assignmentForm = null;
    }
  };

  self.addAssignmentStop = function() {
    if (self.assignmentForm != null) {
      var stopId = self.assignmentForm.elements.stopSelector.val();
      if (stopId == 0) { // TBC : Intentional '==' to compare string to int
        // TBC : TODO : Load custom stop info from map
      } else {
        self.assignmentForm.addSavedStopForm(stopId);
      }
    }
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

    elements.btnNewAssignment.on('click', function() {
      self.loadNewAssignmentForm();
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .btn-cancel', function() {
      self.removeAssignmentForm();
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .btn-save', function() {
      // TBC : Submit assignment-form data
      console.log('save clicked');
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .btn-add-stop', function() {
      self.addAssignmentStop();
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .assignment-stop-form .btn-move-up', function() {
      console.log('move up clicked');
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .assignment-stop-form .btn-move-down', function() {
      console.log('move down clicked');
    });

    elements.assignmentCardContainer.on('click', '.assignment-form .assignment-stop-form .btn-remove', function() {
      var formElement = $(this).closest('.assignment-stop-form');
      var index = formElement.data('index');
      self.assignmentForm.assignmentStopForms.splice(index,1);
      self.assignmentForm.bindAssignmentStopData();
    });

    elements.assignmentCardContainer.on('change', '.assignment-form .assignment-stop-form .field-stop-order', function() {
      console.log('order changed');
    });
  };

  self.initialize();
  return self;
}

function Assignment(data) {
  var self = this;
  var templateId = '#assignment-details-card-template';
  var timeUtils = new TimeUtils();

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
    self.elements.startTime.html(timeUtils.formatTime(self.data.startTime));

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
      estArrive.html(timeUtils.formatTime(report.assignmentStops[i].estArriveTime));
      estWait.html(timeUtils.formatWait(report.assignmentStops[i].estWaitTime));
      estDepart.html(timeUtils.formatTime(report.assignmentStops[i].estDepartTime));

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

  self.initialize();
  return self;
}

function AssignmentForm(selectOptions) {
  var self = this;
  var templateId = '#assignment-form-card-template';

  self.elements = { };
  self.data = { };
  self.assignmentStopForms =[];
  self.selectOptions = selectOptions || { };

  self.initialize = function() {
    self.elements.form = $(templateId).find('.assignment-form').clone();
    self.elements.shuttleSelector = self.elements.form.find('.field-shuttle-select');
    self.elements.driverSelector = self.elements.form.find('.field-driver-select');
    self.elements.routeSelector = self.elements.form.find('.field-route-select');
    self.elements.startTimeSelector = self.elements.form.find('.field-start-time');
    self.elements.stopSelector = self.elements.form.find('.field-stop-select');
    self.elements.scheduleForm = self.elements.form.find('.schedule-form');
    self.elements.scheduleFormTableBody = self.elements.scheduleForm.find('tbody');

    // TBC : Populate components with options
    populateOptions();
    self.elements.startTimeSelector.timepicker({
      'scrollDefault': 'now',
      'timeFormat': 'g:i A'
    });
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {

  };

  self.bindData = function() {

    self.bindAssignmentStopData();
  };

  self.bindAssignmentStopData = function() {
    self.elements.scheduleFormTableBody.empty();

    self.assignmentStopForms.forEach(function(stopForm, index) {
      stopForm.setIndex(index);
      stopForm.bindData();
      self.elements.scheduleFormTableBody.append(stopForm.elements.form);
    });
  };

  self.show = function() {
    self.elements.form.show();
  };

  self.hide = function() {
    self.elements.form.hide();
  };

  self.loadSavedRoute = function(routeId) {
    self.elements.scheduleFormTableBody.empty();
    self.assignmentStopForms = [];

    var routeDetails = self.selectOptions.routeOptions[routeId];
    routeDetails.stops.forEach(function(stopDetails) {
      self.addStopForm(stopDetails);
    });
  };

  self.addSavedStopForm = function(stopId) {
    self.addStopForm(self.selectOptions.stopOptions[stopId]);
  };

  self.addStopForm = function(stopDetails) {
    var stopIndex = self.assignmentStopForms.length;
    var stopForm = new AssignmentStopForm(stopDetails, stopIndex);
    stopForm.show();
    self.elements.scheduleFormTableBody.append(stopForm.elements.form);
    self.assignmentStopForms[stopForm.data.index] = stopForm;
  };

  var populateOptions = function() {
    for (var driverId in selectOptions.driverOptions) {
      populateSelector({
          value: driverId,
          label: selectOptions.driverOptions[driverId]
        }, self.elements.driverSelector
      );
    }

    for (var shuttleId in selectOptions.shuttleOptions) {
      populateSelector({
           value: shuttleId,
           label: selectOptions.shuttleOptions[shuttleId]
         }, self.elements.shuttleSelector
      );
    }

    for (var routeId in selectOptions.routeOptions) {
      populateSelector({
           value: routeId,
           label: selectOptions.routeOptions[routeId].name
         }, self.elements.routeSelector
      );
    }

    for (var stopId in selectOptions.stopOptions) {
      populateSelector({
           value: stopId,
           label: selectOptions.stopOptions[stopId].name
         }, self.elements.stopSelector
      );
    }
  };

  var populateSelector = function(map, selector) {
    var option = $('<option>');
    option.val(map.value);
    option.html(map.label);
    selector.append(option);
  };

  self.initialize();
  return self;
}

function AssignmentStopForm(data, index) {
  var self = this;
  var templateId = '#assignment-stop-form-template';

  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.form = $(templateId).find('.assignment-stop-form').clone();
    self.elements.btnMoveUp = self.elements.form.find('.btn-move-up');
    self.elements.btnMoveDown = self.elements.form.find('.btn-move-down');
    self.elements.stopOrder = self.elements.form.find('.field-stop-order');
    self.elements.name = self.elements.form.find('.field-stop-name');
    self.elements.address = self.elements.form.find('.field-stop-address');
    self.elements.estArriveTime = self.elements.form.find('.field-est-arrive');
    //self.elements.estWaitTime = self.elements.form.find('.field-est-wait');
    self.elements.estDepartTime = self.elements.form.find('.field-est-depart');
    self.elements.btnRemove = self.elements.form.find('.btn-remove');

    self.elements.estArriveTime.timepicker({
                                 'scrollDefault': 'now',
                                 'timeFormat': 'g:i A'
                               });

    // self.elements.estArriveTime.timepicker({
    //                              'scrollDefault': 'now',
    //                              'timeFormat': 'g:i A'
    //                            });

    self.elements.estDepartTime.timepicker({
                                 'scrollDefault': 'now',
                                 'timeFormat': 'g:i A'
                               });

    self.setIndex(index);
    self.update(data);
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setIndex = function(index) {
    self.data.index = index;
  };

  self.setData = function(data) {
    self.data.id = data.id;
    self.data.name = data.name;
    self.data.address = data.address;
  };

  self.bindData = function() {
    self.elements.form.data('index', self.data.index);
    self.elements.stopOrder.val(self.data.index + 1);
    self.elements.name.html(self.data.name);
    self.elements.address.html(self.data.address);
  };

  self.show = function() {
    self.elements.form.show();
  };

  self.hide = function() {
    self.elements.form.hide();
  };

  self.initialize();
  return self;
}
