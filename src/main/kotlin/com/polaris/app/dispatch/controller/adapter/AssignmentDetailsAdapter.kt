package com.polaris.app.dispatch.controller.adapter

import java.time.LocalTime


class AssignmentDetailsAdapter {
    var shuttleId: Int? = null
    var shuttleName: String = ""
    var driverId: Int? = null
    var driverName: String = ""
    var routeId: Int? = null
    var routeName: String = ""
    var startTime: LocalTime? = null
    var assignmentReport: AssignmentReport? = AssignmentReport()
    val currentStopName: String? by lazy {
        if (this.assignmentReport == null) {
            null
        } else {
            this.assignmentReport!!.assignmentStops[this.assignmentReport!!.currentStop].name
        }
    }
}