function GoogleMapsLoader() {
  var self = this;

  var deferred = $.Deferred();
  var baseuUrl = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyDCMCmjl0mitA-dr-aEQdFMGtFNeIQwF7g&callback=';
  var globalCallbackName = 'googleMapsLoaded_' + Math.floor(Math.random() * 10000);

  window[globalCallbackName] = function() {
    deferred.resolve();
    delete window[globalCallbackName];
  };

  self.load = function() {
    var script = $('<script>');
    script[0].attributes.async = 'async';
    script[0].async.value = 'async';
    script[0].defer = 'defer';
    script[0].defer.value = 'defer';
    script[0].src = baseuUrl + globalCallbackName;
    $('body').append(script);

    return deferred.promise();
  };
}
