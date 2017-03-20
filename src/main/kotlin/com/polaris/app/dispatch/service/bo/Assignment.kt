package com.polaris.app.dispatch.service.bo

import java.time.LocalTime

data class Assignment(
        val assignmentID: Int,
        val startTime: LocalTime,
        val routeName: String,
        val driverID: Int,
        val shuttleID: Int,
        val stops: List<AssignmentStop>
)