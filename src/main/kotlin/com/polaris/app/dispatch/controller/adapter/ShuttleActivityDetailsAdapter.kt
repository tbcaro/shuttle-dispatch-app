package com.polaris.app.dispatch.controller.adapter

import com.polaris.app.dispatch.controller.adapter.enums.ShuttleState
import java.math.BigDecimal


class ShuttleActivityDetailsAdapter {
    var activityId: Int = 0
    var shuttleName: String = ""
    var shuttleColorHex: String = "#000000"
    var shuttleLatitude: BigDecimal = BigDecimal("0")
    var shuttleLongitude: BigDecimal = BigDecimal("0")
    var shuttleHeading: BigDecimal = BigDecimal("0")
    var driverName: String = ""
    var shuttleStatus: ShuttleState = ShuttleState.NONE
    var assignmentReport: AssignmentReport? = AssignmentReport()
    val currentStopName: String? by lazy {
        if (this.assignmentReport == null) {
            null
        } else {
            this.assignmentReport!!.assignmentStops[this.assignmentReport!!.currentStop].name
        }
    }
}