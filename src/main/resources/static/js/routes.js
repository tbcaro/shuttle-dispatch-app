
function RouteApp() {
  var self = this;
  var elements = { };
  var routeCards = { };
  var routeForm = null;
  var editingRouteId = null;

  self.initialize = function() {
    // TBC : Setup elements
    elements.routeCardContainer = $('#route-card-container');
    elements.btnNewRoute = $('#btn-new-route');

    fetchAllRoutes();
    bindEventHandlers();
  };

  var bindEventHandlers = function() {
    elements.btnNewRoute.on('click', function() {
      loadNewRouteForm();
    });

    elements.routeCardContainer.on('click', '.route-card .btn-edit', function() {
      var routeId = $(this).closest('.route-card').data('routeId');
      var routeCard = routeCards[routeId];

      if (editingRouteId != null) {
        routeCards[editingRouteId].show();
      }

      editingRouteId = routeId;
      routeCard.hide();
      loadNewRouteForm(routeCard.getData());
    });

    elements.routeCardContainer.on('click', '.route-card .btn-archive', function() {
      var routeId = $(this).closest('.route-card').data('routeId');
      var routeCard = routeCards[routeId];

      routeCard.hide();
      archiveRoute(routeId);
    });

    elements.routeCardContainer.on('click', '.route-form .btn-cancel', function() {
      removeRouteForm();

      if (editingRouteId != null) {
        routeCards[editingRouteId].show();
        editingRouteId = null;
      }
    });

    elements.routeCardContainer.on('click', '.route-form .btn-save', function() {
      saveRoute();
    });

    elements.routeCardContainer.on('click', '.route-form .btn-add-stop', function() {
      addStop();
    });

    elements.routeCardContainer.on('click', '.route-form .stop-form .btn-move-up', function() {
      var formElement = $(this).closest('.stop-form');
      var index = formElement.data('index');

      if (index - 1 >= 0) {
        var tempForm = routeForm.stopForms.splice(index, 1)[0];
        routeForm.stopForms.splice(index - 1, 0, tempForm);
      }

      routeForm.bindAssignmentStopData();
    });

    elements.routeCardContainer.on('click', '.route-form .stop-form .btn-move-down', function() {
      var formElement = $(this).closest('.stop-form');
      var index = formElement.data('index');

      if (index + 1 <= routeForm.stopForms.length - 1) {
        var tempForm = routeForm.stopForms.splice(index, 1)[0];
        routeForm.stopForms.splice(index + 1, 0, tempForm);
      }

      routeForm.bindStopData();
    });

    elements.routeCardContainer.on('click', '.route-form .stop-form .btn-remove', function() {
      var formElement = $(this).closest('.stop-form');
      var index = formElement.data('index');
      routeForm.stopForms.splice(index,1);
      routeForm.bindStopDate();
    });

    elements.routeCardContainer.on('change', '.route-form .stop-form .field-stop-order', function() {
      try {
        var formElement = $(this).closest('.stop-form');
        var curIndex = formElement.data('index');
        var newIndex = Number.parseInt(formElement.find('.field-stop-order').val()) - 1;

        if (newIndex >= 0 && newIndex <= routeForm.stopForms.length - 1) {
          var tempForm = routeForm.stopForms.splice(curIndex,1)[0];
          routeForm.stopForms.splice(newIndex, 0, tempForm);
        }

        routeForm.bindStopDate();
      } catch (ex) {
        console.log(ex);
      }
    });
  };

  var fetchAllRoutes = function() {
    axios.get('/api/fetchAllRoutes')
        .then(function(response){
          console.log(response);
          updateRoutes(response.data);
        })
        .catch(function(error){
          console.log(error);
        });
  };

  var loadNewRouteForm = function(routeData) {
    axios.get('/api/route/formOptions')
        .then(function(response){
          console.log(response);
          addRouteForm(response.data, routeData);
        })
        .catch(function(error){
          console.log(error);
        });
  };

  var saveRoute = function() {
    axios.post('/api/route/save', routeForm.getFormData())
        .then(function(response) {
          fetchAllRoutes();
        })
        .catch(function(error) {
          console.log(error);
          alert(error.message());
        });
  };

  var archiveRoute = function(routeId) {
    axios.post('/api/route/archive', { routeId: routeId })
        .then(function(response) {
          fetchAllRoutes();
        })
        .catch(function(error) {
          console.log(error);
          alert(error.message());
        });
  };

  var updateRoutes = function(routes) {
    routeCards = { };
    elements.routeCardContainer.empty();
    routes.forEach(function(route) {
      routeCards[route.routeId] = new Route(route);
      elements.routeCardContainer.append(routeCards[route.routeId].elements.card);
      routeCards[route.routeId].show();
    });
  };

  var addRouteForm = function(options, routeData) {
    removeRouteForm();

    routeForm = new RouteForm(options);

    if (routeData != null) {
      routeForm.update(routeData);
    }

    elements.routeCardContainer.prepend(routeForm.elements.form);
    routeForm.elements.txtBoxRouteName[0].focus(); // TBC : Set focus to the element
  };

  var removeRouteForm = function() {
    if (routeForm != null) {
      routeForm.elements.form.remove();
      routeForm = null;
    }
  };

  var addStop = function() {
    if (routeForm != null) {
      var stopId = routeForm.elements.stopSelector.val();
      routeForm.addSavedStopForm(stopId);
    }
  };

  self.initialize();
  return self;
}

function Route(data) {
  var self = this;
  var templateId = '#route-details-card-template';

  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.card = $(templateId).find('.route-card').clone();
    self.elements.routeName = self.elements.card.find('.field-route-name');
    self.elements.scheduleCard = self.elements.card.find('.schedule-card');
    self.elements.scheduleTableBody = self.elements.scheduleCard.find('tbody');

    self.update(data);
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.routeId = data.routeId;
    self.data.routeName = data.name;
    self.data.stops = data.stops;
  };

  self.getData = function() {
    return {
      routeId: self.data.routeId,
      routeName: self.data.routeName,
      stops: self.data.stops
    }
  };

  self.bindData = function() {
    self.elements.card.data('routeId', self.data.routeId);
    self.elements.routeName.html(self.data.routeName);

    // TBC : Bind stop data to table
    if (self.data.stops != null) {
      self.bindStopData();
    }
  };

  self.bindStopData = function() {
    self.elements.scheduleTableBody.empty();
    var stops = self.data.stops;
    for (var i = 0; i < stops.length; i++) {
      var row = $('<tr>');

      var order = $('<td>');
      var stopName = $('<td>');
      var address = $('<td>');
      var latitude = $('<td>');
      var longitude = $('<td>');

      order.html(i + 1);
      stopName.html(stops[i].name);
      address.html(stops[i].address);
      latitude.html(stops[i].lat);
      longitude.html(stops[i].long);

      row.append(order);
      row.append(stopName);
      row.append(address);
      row.append(latitude);
      row.append(longitude);

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

function RouteForm(selectOptions) {
  var self = this;
  var templateId = '#assignment-form-card-template';
  var timeUtils = new TimeUtils();

  self.elements = { };
  self.data = { };
  self.stopForms =[];
  self.selectOptions = selectOptions || { };

  self.initialize = function() {
    self.elements.form = $(templateId).find('.route-form').clone();
    self.elements.txtBoxRouteName = self.elements.form
    // self.elements.shuttleSelector = self.elements.form.find('.field-shuttle-select');
    // self.elements.driverSelector = self.elements.form.find('.field-driver-select');
    // self.elements.routeSelector = self.elements.form.find('.field-route-select');
    // self.elements.startTimeSelector = self.elements.form.find('.field-start-time');
    self.elements.stopSelector = self.elements.form.find('.field-stop-select');
    self.elements.scheduleForm = self.elements.form.find('.schedule-form');
    self.elements.scheduleFormTableBody = self.elements.scheduleForm.find('tbody');

    // TBC : Populate components with options
    populateOptions();
    // self.elements.startTimeSelector.timepicker({
    //                                              'scrollDefault': 'now',
    //                                              'timeFormat': 'g:i A'
    //                                            });
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.routeId = data.routeId;
    // self.data.selectedShuttleId = data.shuttleId;
    // self.data.selectedDriverId = data.driverId;
    // self.data.selectedRouteId = data.routeId;
    // self.data.selectedStartTime = data.startTime;

    data.assignmentReport.assignmentStops.forEach(function(stopData) {
      var form = new AssignmentStopForm(stopData, stopData.order);
      self.stopForms[form.data.index] = form;
    });
  };

  self.getFormData = function(selectedDate) {
    var formData = {
      routeId: { },
      shuttleId: { },
      driverId: { },
      routeId: { },
      startTime: { },
      stopForms: { }
    };
    var assignmentStopData = [];

    self.stopForms.forEach(function(stopForm) {
      assignmentStopData.push(stopForm.getFormData(selectedDate));
    });

    var startTime = moment.utc(
        selectedDate.format('YYYY-MM-DD') + ' ' +
        self.elements.startTimeSelector.val().toString()
        , ["YYYY-MM-DD h:mm A", "YYYY-MM-DD hh:mm A"]
        , true);

    formData.routeId.value = self.elements.form.data('routeId');
    formData.shuttleId.value = self.elements.shuttleSelector.val();
    formData.driverId.value = self.elements.driverSelector.val();
    formData.routeId.value = self.elements.routeSelector.val();
    startTime.isValid() ? formData.startTime.value = startTime.toISOString() : formData.startTime.value = null;
    formData.stopForms = assignmentStopData;

    return formData;
  };

  self.bindData = function() {
    self.elements.form.data('routeId', self.data.routeId);
    self.elements.shuttleSelector.val(self.data.selectedShuttleId);
    self.elements.driverSelector.val(self.data.selectedDriverId);
    self.elements.routeSelector.val(self.data.selectedRouteId);
    self.elements.startTimeSelector.val(timeUtils.formatTime(self.data.selectedStartTime));

    self.bindStopDate();
  };

  self.bindStopDate = function() {
    self.elements.scheduleFormTableBody.empty();

    self.stopForms.forEach(function(stopForm, index) {
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
    self.stopForms = [];

    var routeDetails = self.selectOptions.routeOptions[routeId];
    routeDetails.stops.forEach(function(stopDetails) {
      self.addStopForm(stopDetails);
    });
  };

  self.addSavedStopForm = function(stopId) {
    self.addStopForm(self.selectOptions.stopOptions[stopId]);
  };

  self.addStopForm = function(stopDetails) {
    var stopIndex = self.stopForms.length;
    var stopForm = new AssignmentStopForm(stopDetails, stopIndex);
    stopForm.show();
    self.elements.scheduleFormTableBody.append(stopForm.elements.form);
    self.stopForms[stopForm.data.index] = stopForm;
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
