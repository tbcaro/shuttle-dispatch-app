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
    var assignmentReport: AssignmentReport = AssignmentReport()
    val currentStopName: String by lazy {
        this.assignmentReport.stops[this.assignmentReport.currentStop].name
    }
    val isActive: Boolean by lazy {
        this.shuttleStatus == ShuttleState.ACTIVE
    }
    val isDriving: Boolean by lazy {
        this.shuttleStatus == ShuttleState.DRIVING
    }
    val isAtStop: Boolean by lazy {
        this.shuttleStatus == ShuttleState.AT_STOP
    }
}