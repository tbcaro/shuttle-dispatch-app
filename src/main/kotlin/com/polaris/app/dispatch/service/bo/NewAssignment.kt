package com.polaris.app.dispatch.service.bo

import java.time.LocalDate
import java.time.LocalTime

data class NewAssignment(
        val serviceID: Int?,
        val driverID: Int?,
        val shuttleID: Int?,
        val routeID: Int?,
        val startDate: LocalDate?,
        val startTime: LocalTime?,
//        val routeName: String?,
        val stops: List<NewAssignmentStop>
)