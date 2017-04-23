function GeoLocator() {
  return {
    getLocation: function() {
      var deferred = $.Deferred();

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          var location = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };

          deferred.resolve(location);
        }, deferred.reject, { maximumAge: 0, timeout: 30000, enableHighAccuracy: true });
      } else {
        deferred.reject("Your browser does not support geolocation");
      }

      return deferred.promise();
    }
  }
}
