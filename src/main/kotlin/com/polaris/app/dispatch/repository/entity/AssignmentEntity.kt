package com.polaris.app.dispatch.repository.entity

import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState
import java.time.LocalTime
import java.time.LocalDate

data class AssignmentEntity(
        val assignmentID: Int,
        val serviceID: Int,
        val startDate: LocalDate?,
        val startTime: LocalTime?,
        val routeID: Int?,
        val routeName: String?,
        val driverID: Int,
        val driverFName: String,
        val driverLName: String,
        val shuttleID: Int,
        val shuttleName: String,
        val status: AssignmentState
)