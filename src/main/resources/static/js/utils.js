function TimeUtils() {
  var self = this;

  self.formatTime = function(localTime) {
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

  self.formatWait = function(waitMins) {
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

  return self;
}