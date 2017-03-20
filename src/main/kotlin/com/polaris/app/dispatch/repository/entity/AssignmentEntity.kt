package com.polaris.app.dispatch.repository.entity

import java.time.LocalTime

data class AssignmentEntity(
        val assignmentID: Int,
        val startTime: LocalTime,
        val routeName: String,
        val driverID: Int,
        val shuttleID: Int
)