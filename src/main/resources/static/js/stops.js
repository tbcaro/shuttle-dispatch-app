
function StopApp(options) {
  const GEOCODER_API_KEY = 'AIzaSyBZDbTJLMcIKnLDV1m9gNhZLu4vjKyeTQo';

  var self = this;
  var elements = { };
  var geoLocator = { };
  var map = { };
  var mapMarkers = { };
  var stopCards = { };
  var stopForm = null;
  var editingStopId = null;
  var formattedCurrentStopAddress = null;

  self.initialize = function() {
    // TBC : Setup elements
    elements.mapContainer = $('#map-container');
    elements.controlPanel = $('#control-panel');
    elements.stopCardContainer = $('#stop-card-container');
    elements.txtBoxAddress = elements.controlPanel.find('#txtbox-address');
    elements.btnSearchAddress = elements.controlPanel.find('#btn-search');
    elements.btnNewStop = elements.controlPanel.find('#btn-new-stop');

    // TBC : Setup utility objects
    geoLocator = new GeoLocator();
    initializeMap();
    geoLocator.getLocation().done(function(location) {
      map.setCenter(location);
      map.setZoom(12);
      mapMarkers.stopMarker = new google.maps.Marker({
        position: location,
        map: map,
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

    fetchAllStops();
    bindEventHandlers();
  };

  var initializeMap = function() {
    map = new google.maps.Map(elements.mapContainer[0], {
      center: { lat: 39.8282, lng: -98.5795 }, // TBC : Default center over central USA
      zoom: 5
    });
  };

  var bindEventHandlers = function() {

  };

  var fetchAllStops = function() {
    axios.get('/api/fetchAllStops')
        .then(function(response){
          console.log(response);
          updateStops(response.data);
        })
        .catch(function(error){
          console.log(error);
        });
  };

  var saveStop = function() {
    axios.post('/api/stop/save', stopForm.getFormData())
        .then(function(response) {
          fetchAllStops();
        })
        .catch(function(error) {
          console.log(error);
          alert(error.message());
        });
  };

  var archiveStop = function(stopId) {
    axios.post('/api/stop/archive', { stopId: stopId })
        .then(function(response) {
          fetchAllStops();
        })
        .catch(function(error) {
          console.log(error);
          alert(error.message());
        });
  };

  var updateStops = function(stops) {
    stopCards = { };
    elements.stopCardContainer.empty();
    stops.forEach(function(stop) {
      stopCards[stop.stopId] = new Stop(stop);
      elements.stopCardContainer.append(stopCards[stop.stopId].elements.card);
      stopCards[stop.stopId].show();
    });
  };

  var addStopForm = function(options, stopData) {
    removeStopForm();

    stopForm = new StopForm(options);

    if (stopData != null) {
      stopForm.update(stopData);
    }

    elements.stopCardContainer.prepend(stopForm.elements.form);
    stopForm.elements.txtBoxStopName[0].focus(); // TBC : Set focus to the element
  };

  var removeStopForm = function() {
    if (stopForm != null) {
      stopForm.elements.form.remove();
      stopForm = null;
    }
  };

  self.initialize();
  return self;
}

function Stop(data) {
  var self = this;
  var templateId = '#stop-details-card-template';

  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.card = $(templateId).find('.stop-card').clone();
    self.elements.stopName = self.elements.card.find('.field-stop-name');
    self.elements.stopAddress = self.elements.card.find('.field-stop-address');
    self.elements.stopLat = self.elements.card.find('.field-stop-lat');
    self.elements.stopLong = self.elements.card.find('.field-stop-long');

    self.update(data);
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.stopId = data.stopId;
    self.data.name = data.name;
    self.data.address = data.address;
    self.data.lat = data.lat;
    self.data.long = data.long;
  };

  self.getData = function() {
    return {
      stopId: self.data.stopId,
      name: self.data.name,
      address: self.data.address,
      lat: self.data.lat,
      long: self.data.long
    }
  };

  self.bindData = function() {
    self.elements.card.data('stopId', self.data.stopId);
    self.elements.stopName.html(self.data.name);
    self.elements.stopAddress.html(self.data.address);
    self.elements.stopLat.html(self.data.lat);
    self.elements.stopLong.html(self.data.long);
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

function StopForm() {
  var self = this;
  var templateId = '#stop-form-card-template';

  self.elements = { };
  self.data = { };

  self.initialize = function() {
    self.elements.form = $(templateId).find('.stop-form').clone();
    self.elements.txtBoxStopName = self.elements.form.find('.field-stop-name');
    self.elements.stopSelector = self.elements.form.find('.field-stop-select');
    self.elements.scheduleForm = self.elements.form.find('.schedule-form');
    self.elements.scheduleFormTableBody = self.elements.scheduleForm.find('tbody');

    // TBC : Populate components with options
    populateOptions();
  };

  self.update = function(data) {
    self.setData(data);
    self.bindData();
  };

  self.setData = function(data) {
    self.data.stopId = data.stopId;
    self.data.stopName = data.stopName;

    data.stops.forEach(function(stopData) {
      var form = new StopStopForm(stopData, stopData.index);
      self.stopForms[form.data.index] = form;
    });
  };

  self.getFormData = function() {
    var formData = {
      stopId: { },
      stopName: { },
      stopStopForms: { }
    };
    var stopData = [];

    self.stopForms.forEach(function(stopForm) {
      stopData.push(stopForm.getFormData());
    });

    formData.stopId.value = self.elements.form.data('stopId');
    formData.stopName.value = self.elements.txtBoxStopName.val();
    formData.stopStopForms = stopData;

    return formData;
  };

  self.bindData = function() {
    self.elements.form.data('stopId', self.data.stopId);
    self.elements.txtBoxStopName.val(self.data.stopName);

    self.bindStopData();
  };

  self.bindStopData = function() {
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

  self.addSavedStopForm = function(stopId) {
    self.addStopForm(self.selectOptions.stopOptions[stopId]);
  };

  self.addStopForm = function(stopDetails) {
    var stopIndex = self.stopForms.length;
    var stopForm = new StopStopForm(stopDetails, stopIndex);
    stopForm.show();
    self.elements.scheduleFormTableBody.append(stopForm.elements.form);
    self.stopForms[stopForm.data.index] = stopForm;
  };

  var populateOptions = function() {
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