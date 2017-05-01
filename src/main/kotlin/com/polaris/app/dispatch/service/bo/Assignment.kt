package com.polaris.app.dispatch.service.bo

import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import java.time.LocalTime

data class Assignment(
        val assignmentID: Int,
        val startTime: LocalTime?,
        val routeID: Int?,
        val routeName: String?,
        val driverID: Int,
        val driverFName: String,
        val driverLName: String,
        val shuttleID: Int,
        val shuttleName: String,
        val stops: List<AssignmentStop>,
        val status: AssignmentState
)