
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
      self.highlightSelectedCard($(this));
    });
  };

  self.highlightSelectedCard = function(card) {
    var selected = elements.shuttleCardContainer.find('.selected');

    selected.removeClass('selected');
    card.addClass('selected');
  };

  self.updateShuttleCards = function(data) {
    data.forEach(function(activity) {
      // TBC : If activity has already been loaded, update it.
      if (self.shuttleActivities.hasOwnProperty(activity.activityId.toString())) {
        self.shuttleActivities[activity.activityId].updateData(activity);
        self.shuttleActivities[activity.activityId].element.replaceWith(self.generateElement());
      } else { // TBC : If activity is not present, create it
        var shuttleActivity = new ShuttleActivity(activity, shuttleActivityCardtemplate);
        self.shuttleActivities[shuttleActivity.activityId] = shuttleActivity;
        elements.shuttleCardContainer.append(shuttleActivity.element);
      }
    });
  };

  self.initialize();
  return self;
}

function ShuttleActivity(serializedActivity, template) {
  var self = this;

  self.element;
  self.data = { };

  self.initialize = function() {
    self.updateData(serializedActivity);

    // TBC : Generate element from template
    self.element = self.generateElement();
  };

  self.updateData = function(activity) {
    for (var key in activity) {
      self.data[key] = activity[key];
    }
  };

  self.generateElement = function() {
    return $(template(self.data)).filter('.shuttle-card');
  };
  //
  // self.bindEventHandlers = function() {
  //   self.element.on('click', function() {
  //     self.toggleSelected();
  //   });
  // };
  //
  // self.toggleSelected = function() {
  //   if (self.isSelected)  {
  //     self.isSelected = false;
  //     self.element.removeClass('selected');
  //   } else {
  //     self.isSelected = true;
  //     self.element.addClass('selected');
  //   }
  // };

  self.initialize();
  return self;
}