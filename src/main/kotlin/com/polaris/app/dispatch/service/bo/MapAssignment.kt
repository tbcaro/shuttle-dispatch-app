package com.polaris.app.dispatch.service.bo

data class MapAssignment(
        val assignmentID: Int,
        val driverID: Int,
        val driverFName: String,
        val driverLName: String,
        val shuttleID: Int,
        val shuttleName: String,
        val stops: List<MapAssignmentStop>
)