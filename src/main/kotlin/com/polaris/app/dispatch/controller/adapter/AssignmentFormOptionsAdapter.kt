package com.polaris.app.dispatch.controller.adapter

import java.util.*


class AssignmentFormOptionsAdapter {
    var shuttleOptions: MutableMap<Int, String> = HashMap()
    var driverOptions: MutableMap<Int, String> = HashMap()
    var routeOptions: MutableMap<Int, RouteDetailsAdapter> = HashMap()
    var stopOptions: MutableMap<Int, StopDetailsAdapter> = HashMap()
}